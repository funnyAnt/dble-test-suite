# 2. C-MySQL-API测试

## Makefile中的mysql相关文件路径请相应修改：
 INCLUDES := -I/usr/local/mysql/include
 LIBDIRS  :=-L/usr/local/mysql/lib

## 执行：
bash c_mysql_api.sh  
注：multiQuery.sh 中执行了 清理、编译、运行和结果比对

## 可能遇到的问题及解决	：
 - 1, g++找不到
 
   yum install gcc-c++
   
 - 2,libmysqlclient.so.20找不到
 
   /etc/ld.so.conf.d/mariadb-x86_64.conf，添加mysql lib的路径并执行 ldconfig
 
## 不使用Makefile,手动编译（相关mysql文件夹路径自行替换）：
g++ -g *.cpp -o c_mysql_api.o -L/usr/local/mysql/lib -I/usr/local/mysql/include -lmysqlclient
## 调试：
./c_mysql_api.o 