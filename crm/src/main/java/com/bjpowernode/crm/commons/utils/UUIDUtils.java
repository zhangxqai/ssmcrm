package com.bjpowernode.crm.commons.utils;

import java.util.Date;
import java.util.UUID;

public class UUIDUtils {

        /**
         * 生成一个36位的字符串，toString是转换为字符串，rep...是换，"-"是要换的，""是要换成什么的
         * @return
         */
        public static String getUUID(){

                return UUID.randomUUID().toString().replaceAll("-","");

        }
        /*public static String formatDateTime(new Date()){

                return UUID.randomUUID().toString().replaceAll("-","");

        }*/


}
