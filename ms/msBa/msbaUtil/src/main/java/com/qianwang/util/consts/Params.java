package com.qianwang.util.consts;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/4/22.
 */
public class Params{
    static public String rechargeAmountBelow20 = "1100001" ;
    static public String rechargeAmount20To100 = "1100002" ;
    static public String rechargeAmount100To500 = "1100003" ;
    static public String rechargeAmount500To1000 = "1100004" ;
    static public String rechargeAmount1000To10000 = "1100005" ;
    static public String rechargeAmountUp10000 = "1100006" ;

    static public String trueRechargeAmountBelow20 = "1100001" ;
    static public String trueRechargeAmount20To100 = "1100002" ;
    static public String trueRechargeAmount100To500 = "1100003" ;
    static public String trueRechargeAmount500To1000 = "1100004" ;
    static public String trueRechargeAmount1000To10000 = "1100005" ;
    static public String trueRechargeAmount10000To50000 = "1100006" ;
    static public String trueRechargeAmount50000To100000 = "1100007" ;
    static public String trueRechargeAmountUp100000 = "1100008" ;

    static public String transferAmount20To100 = "1200001" ;
    static public String transferAmount100To900 = "1200002" ;
    static public String transferAmount900To5000 = "1200003" ;
    static public String transferAmount5000To10000 = "1200004" ;
    static public String transferAmount10000To50000 = "1200005" ;
    static public String transferAmount50000To100000 = "1200006" ;
    static public String transferAmountUp100000 = "1200007" ;

    static public String rechargeSustain1Day = "2900001" ;
    static public String rechargeSustain7Day = "2900002" ;
    static public String rechargeSustain8To30Day = "2900003" ;
    static public String rechargeSustain30To90Day = "2900004" ;
    static public String rechargeSustainUp90Day = "2900005" ;

    static public String amountRatio0To5 = "3000001" ;
    static public String amountRatio5To50 = "3000002" ;
    static public String amountRatio50TO95 = "3000003" ;
    static public String amountRatio95TO100 = "3000004" ;

    static public String assetClass0To30 = "3100001";
    static public String assetClass30To100 = "3100002";
    static public String assetClass100To10000 = "3100003";
    static public String assetClass10000To50000 = "3100004";
    static public String assetClass50000To100000 = "3100005";
    static public String assetClassUp100000 = "3100006";

    static public HashMap<Integer,String> constInfo = new HashMap<Integer, String>(){
        {
            //注册时长
            put(2900001, "1天");
            put(2900002, "2-7天");
            put(2900003, "8-30天");
            put(2900004, "31-90天");
            put(2900005, "90天以上");
            //用户消费新
            put(1, "微商");
            put(2, "雷拍");
            put(3, "宝购");
            put(4,"解换绑手续费");
            put(5, "有票支付");
            put(6, "酷雅");
            put(7, "账户管理费");
            put(8,"藤榕");
            put(9,"游戏");
            put(10,"发红包手续费");
            put(11,"任务及分销罚金");
            put(12,"充值手续费");
            put(13,"提现手续费");
            put(14,"其他");

            //用户奖励老的
            put(-151, "提现的注册奖励");
            put(64, "签到固定收益");
            put(66,"签到浮动收益");
            put(300,"推广达人奖励");
            put(4, "任务收益");
            put(112, "抢红包");
            put(217, "新手签到");
            put(252,"任务分红");
            put(292,"分享任务");
            put(10000,"其他任务");
            put(55,"手动结束任务");
            put(-252, "其他");

            //用户奖励新的
//            put(64, "签到固定收益");
//            put(66,"签到浮动收益");
//            put(300,"推广达人奖励");
//            put(4, "任务收益");
            put(117, "分销收益");
            put(97, "抢红包宝币收益");
            put(124, "获取宝约奖励");
//            put(-252, "其他");
            //消费分档
            put(3400001,"(0,20]");
            put(3400002,"(20,100]");
            put(3400003,"(100,500]");
            put(3400004,"(500,1000]");
            put(3400005,"(1000,+oo)");
            //奖励分档
            put(3300001,"(0,18)");
            put(3300002,"[18,20)");
            put(3300003,"[20,100)");
            put(3300004,"[100,1000)");
            put(3300005,"[1000,+oo)");
        }
    };
    static public HashMap<Integer,String> constColorInfo = new HashMap<Integer, String>(){
        {
            //注册时长
            put(2900001, "blue");
            put(2900002, "#ff7f0e");
            put(2900003, "#2ca02c");
            put(2900004, "#d62728");
            put(2900005, "#9467bd");
            //用户消费新
            put(1, "blue");
            put(2, "#ff7f0e");
            put(3, "#2ca02c");
            put(4,"#d62728");
            put(5, "#9467bd");
            put(6, "#8c564b");
            put(7, "#e377c2");
            put(8,"#7f7f7f");
            put(9,"purple");
            put(10,"red");
            put(11,"orange");
            put(12,"#aa363d");
            put(13,"#df9464");
            put(14,"#1d953f");

            //用户消费旧
            put(-200001, "blue");
            put(50, "#ff7f0e");
            put(5, "#2ca02c");
            put(280,"#d62728");
            put(206, "#9467bd");
            put(400, "#8c564b");
            put(-250, "#e377c2");

            put(360,"#7f7f7f");
            put(80,"purple");
            put(293,"red");
            put(700007,"orange");
            //用户奖励老的
            put(-151, "blue");
            put(64, "#ff7f0e");
            put(66,"#2ca02c");
            put(300,"#d62728");
            put(4, "#9467bd");
            put(112, "#8c564b");
            put(217, "#e377c2");
            put(252,"yellow");
            put(292,"orange");
            put(10000,"pink");
            put(55,"purple");
            put(-252, "#CD919E");
            //用户奖励新的
//            put(113, "#ff7f0e");
//            put(114,"#2ca02c");
//            put(35,"#d62728");
//            put(109, "#9467bd");
            put(117, "#6b473c");
            put(97, "#7fb80e");
            put(124, "#6f60aa");
//            put(-100, "#65c294");
            //消费分档
            put(3400001,"blue");
            put(3400002,"#ff7f0e");
            put(3400003,"#2ca02c");
            put(3400004,"#d62728");
            put(3400005,"#9467bd");
            //奖励分档
            put(3300001,"blue");
            put(3300002,"#ff7f0e");
            put(3300003,"#2ca02c");
            put(3300004,"#d62728");
            put(3300005,"#9467bd");
        }
    };
}