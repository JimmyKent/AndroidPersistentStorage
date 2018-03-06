## <center>公有目录 vs 私有目录</center>

### 私有目录
data/data/apkname/

对于没有 Root 过的手机，普通用户是无法查看 data/data 目录内容的。  
代码获取其中的路径  
使用context可以直接调用：  
* getFilesDir() // /storage/emulated/0/Android/data/apkname/files  
* getCacheDir()  
* deleteFile()  
* fileList()  
等方法，也可以通过 Environment 类访问：  
* Environment.getDataDirectory();

### 外部存储空间的应用私有目录(简称半私有)
/storage/emulated/0/Android/data/apk_name/

代码获取其中的路径  
使用context可以直接调用：  
* getExternalFilesDir()
* getExternalCacheDir()
等方法，也可以通过 Environment 类访问：  
* Environment.getExternalStorageDirectory();

1. 默认情况下，系统并不会自动创建半私有。只有在应用需要的时候，开发人员通过 SDK 提供的 API 创建该目录文件夹和操作文件夹内容。  
2. 版本问题: 自 Android 7.0 开始，系统对应用私有目录的访问权限进一步限制。其他 App 无法通过 file:// 这种形式的 Uri 直接读写该目录下的文件内容，而是通过 FileProvider 访问。  
3. 宿主 App 可以直接读写内部存储空间中的应用私有目录；而在 4.4 版本开始，宿主 App 才可以直接读写外部存储空间中的应用私有目录，使开发人员无需在 Manifest 文件中或者动态申请外部存储空间的文件读写权限。

> 私有 vs 半私有  
应用卸载的时候,会自动删除私有目录data/data/apkname/ 和半私有 /storage/emulated/0/Android/data/apkname/ 里面的数据  

值得注意的一点是，对于半私有目录文件，由于普通用户可以自由修改和删除，在使用时，一定要做好判空处理和异常捕获，防止应用崩溃退出！


### 公有目录
/storage/emulated/0/

代码获取其中的路径  
```
Environment.getExternalStoragePublicDirectory(String type);
```  
type类型  
DIRECTORY_MUSIC：Music

需要权限: 读READ_EXTERNAL_STORAGE 写WRITE_EXTERNAL_STORAGE

### 其他目录

> /system/app vs /data/app   
/data/app 里软件权限没全开，/system/app 里的软件获取了所有权限  
/data/app 可以应用卸载，/system/app 只能 root 后删除  
/data/app 文件夹大小随便，/system/app 文件夹有大小限制  
卸载/system/app 目录下的文件并不会增加系统空间，即可用 ROM 空间  





