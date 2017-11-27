# USBPrinterDemo

通用型、小票/标签、USB连接模式、便携式打印机及钱箱

Demo为USB连接模式下的小票与标签打印机以及钱箱的开箱操作
指令集已经封装完成，小票为EscCommand，标签为LabelCommand

> * 小票打印，一般都是使用ESC/POS打印指令
> * 标签打印，一般都是使用TSC打印指令
> * 按照连接方式一般分为USB打印机、蓝牙打印机、网络打印机等等
> * 按照纸张大小一般分为带切刀的80厨房打印机和58热敏票据打印机

参考资料

> * [Android USB 主机模式](http://blog.csdn.net/wizardmly/article/details/8350137)(官网 Android USB Host 文档更好)
> * [Android USB 设备如何区分是打印机还是U盘](http://blog.csdn.net/rodulf/article/details/51916998)
> * 另外可以借鉴[佳博的文档](https://github.com/bill556/PrinterDemoDoc)，对于通用型打印机的开发有帮助
> * [Android打印机--小票打印格式及模板设置](http://blog.csdn.net/johnwcheung/article/details/69568231)

感谢CSDN上博主的资料整理与经验分享