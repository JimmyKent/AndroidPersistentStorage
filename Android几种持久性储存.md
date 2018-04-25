
### <center>Android几种持久性储存</center>


特性  
优劣  
时间  
分别合适用作什么  

+ Sp
+ File
+ SQLite  
+ ContentProvider 跨进程  

### <center> SharedPreference </center>  
Android developer地址  
<https://developer.android.com/guide/topics/data/data-storage.html#pref>  
易用的轻量级存储方式, API极其友好,特殊的File,本质是一个xml文件  
实现类 : SharedPreferenceImpl  
特性 : key-value储存  
优劣:API极其简单,使用的Sp文件会加载到内存中  
时间:第一次加载根据文件的大小  
用途:简单的配置,简单的数据  
  
Sp文件过大  
sp在创建的时候会把整个文件全部加载进内存，如果你的sp文件比较大，那么会带来几个严重问题：

1. 第一次从sp中获取值的时候，有可能阻塞主线程，使界面卡顿、掉帧。  
2. 解析sp的时候会产生大量的临时对象，导致频繁GC，引起界面卡顿。  
3. 这些key和value会永远存在于内存之中，占用大量内存。  
 
第一点:getString的时候,有awaitLoadedLocked();  

```
public String getString(String key, @Nullable String defValue) {  
	synchronized (this) {  
        awaitLoadedLocked();  
        String v = (String)mMap.get(key);
        return v != null ? v : defValue;
    }
}
private void awaitLoadedLocked() {
    while (!mLoaded) {
        try {
            mLock.wait();
        } catch (InterruptedException unused) {
        }
    }
}
```

第三点:被加载进来的这些大对象，会永远存在于内存之中，不会被释放。我们看看ContextImpl.在getSharedPreference的时候会把所有的sp放到一个静态变量里面缓存起来.

```
private static ArrayMap<String, ArrayMap<File, SharedPreferencesImpl>> sSharedPrefsCache;

public SharedPreferences getSharedPreferences(File file, int mode) {
    synchronized (ContextImpl.class) {
    final ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
    sp = cache.get(file);
    if (sp == null) {
        sp = new SharedPreferencesImpl(file, mode);
        cache.put(file, sp);
        return sp;
    }
}
```

注意这个static的sSharedPrefsCache，它保存了你所有使用的sp，然后sp里面有一个成员mMap保存了所有的键值对；这样，你程序中使用到的那些个sp永远就呆在内存中，是不是不寒而栗？！


存储JSON等特殊符号很多的value
JSON或者HTML格式存放在sp里面的时候，需要转义，这样会带来很多 & 这种特殊符号，sp在解析碰到这个特殊符号的时候会进行特殊的处理，引发额外的字符串拼接以及函数调用开销。


多次apply
第一，把一个带有await的runnable添加进了QueueWork类的一个队列；第二，把这个写入任务通过enqueueDiskWrite丢给了一个只有单个线程的线程池执行。

```
public void apply() {
	final Runnable awaitCommit = new Runnable() {
		public void run() {
			try {
			    mcr.writtenToDiskLatch.await();
			} catch (InterruptedException ignored) {
			}
			
			if (DEBUG && mcr.wasWritten) {
			    Log.d(TAG, mFile.getName() + ":" + mcr.memoryStateGeneration
			            + " applied after " + (System.currentTimeMillis() - startTime)
			            + " ms");
			}
		}
	};
	QueuedWork.addFinisher(awaitCommit);
	Runnable postWriteRunnable = new Runnable() {
		public void run() {
			awaitCommit.run();
			QueuedWork.removeFinisher(awaitCommit);
		}
	};
	SharedPreferencesImpl.this.enqueueDiskWrite(mcr, postWriteRunnable);
	notifyListeners(mcr);
}
```
到这里一切都OK，在子线程里面写入不会卡UI。但是，你去ActivityThread类的handleStopActivity里看一看：

```
private void handleStopActivity(IBinder token, boolean show, int configChanges, int seq) {
    // 省略无关。。
    // Make sure any pending writes are now committed.
    if (!r.isPreHoneycomb()) {
        QueuedWork.waitToFinish();
    }
   // 省略无关。。
}
public static void waitToFinish() {
    Runnable toFinish;
    while ((toFinish = sPendingWorkFinishers.poll()) != null) {
        toFinish.run();
    }
}
```
还记得这个toFinish的Runnable是啥吗？就是上面那个awaitCommit它里面就一句话，等待写入线程！如果在Activity Stop的时候，已经写入完毕了，那么万事大吉，不会有任何等待，这个函数会立马返回。但是，如果你使用了太多次的apply，那么意味着写入队列会有很多写入任务，而那里就只有一个线程在写。当App规模很大的时候，这种情况简直就太常见了！
因此，虽然apply是在子线程执行的，但是请不要无节制地apply；commit直接在当前线程写入,慎重使用。

优化建议:

	• 强烈建议不要在sp里面存储特别大的key/value, 有助于减少卡顿/anr
	• 请不要高频地使用apply, 尽可能地批量提交;commit直接在主线程操作, 更要注意了
	• 不要使用MODE_MULTI_PROCESS;
	• 高频写操作的key与高频读操作的key可以适当地拆分文件, 由于减少同步锁竞争;
	• 不要一上来就执行getSharedPreferences().edit(), 应该分成两大步骤来做, 中间可以执行其他代码.
	• 不要连续多次edit(), 应该获取一次获取edit(),然后多次执行putxxx(), 减少内存波动; 经常看到大家喜欢封装方法, 结果就导致这种情况的出现.
	• 每次commit时会把全部的数据更新的文件, 所以整个文件是不应该过大的, 影响整体性能;

不要滥用SharedPreference  
<http://www.cnblogs.com/mingfeng002/p/5970221.html>  
全面剖析SharedPreferences  
<http://gityuan.com/2017/06/18/SharedPreferences/>  
</br>
</br>

### <center> File </center>  
Android developer地址  
<https://developer.android.com/reference/java/io/File.html>  
文件和目录的抽象,流访问  
实现类:  
特性:可以储存很多东西:html, xml, json, 序列化, 按行储存  
优劣:储存内容种类多,API复杂,需要自己封装  
时间:根据文件的大小  
用途:储存各种数据  

File的几种读写速度比对  
<http://www.baeldung.com/java-write-to-file>  
《Thinking in Java》  



可以放在公有目录也可以放在私有目录

序列化会存在版本问题,不如json灵活



### <center> SQLite </center>

Android developer地址  
<https://developer.android.com/reference/android/database/sqlite/package-summary.html>  
储存数据量大  
特性:储存数据量大,数据结构相同 ORM框架 
优劣:sql语句  可以查询 ORM框架
时间:与数据库大小无直接关系  
用途:储存结构相同的数据,比如列表类数据  

ORM框架  

可以放在公有目录也可以放在私有目录  
