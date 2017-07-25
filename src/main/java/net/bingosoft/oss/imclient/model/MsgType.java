package net.bingosoft.oss.imclient.model;

/**
 * 消息类别
 * @author kael.
 */
public interface MsgType {
    /**
     * 控制指令
     */
    int CONTROL = 0;
    /**
     * 简单文本
     */
    int TEXT    = 1;
    /**
     * 图片（网盘）
     */
    int IMAGE   = 2;
    /**
     * 文件（网盘）
     */
    int FILE    = 3;
    /**
     * 音频（网盘）
     */
    int AUDIO   = 4;
    /**
     * 视频（网盘）
     */
    int VIDEO   = 5;
    /**
     * 语音（网盘）
     */
    int SOUND   = 6;
    /**
     * 地理位置
     */
    int LOCATION= 7;
    /**
     * 服务号事件消息
     */
    int SNO_ENT = 8;
    /**
     * 分享消息
     */
    int SHARE   = 10;
    /**
     * 通用格式消息
     */
    int FORMAT  = 11;
    /**
     * 通知、公告
     */
    int NOTICE  = 12;
    /**
     * 事件
     */
    int EVENT   = 13;
    /**
     * 新网盘分享的消息
     */
    int PAN_SHARE=14;
    /**
     * 视频聊天消息
     */
    int WEBCOM  = 15;
    /**
     * 抖动消息
     */
    int JITTER  = 16;
    /**
     * 伪装消息
     */
    int PERSONATE=17;
    /**
     * 撤回消息
     */
    int RETRACT = 66;
    /**
     * 混合消息
     */
    int MIXTURE = 96;
    /**
     * 回执消息
     */
    int RECEIPT = 97;
    /**
     * 失败消息
     */
    int FAIL    = 98;
    /**
     * 复杂消息
     */
    int COMPLEX = 99;
}
