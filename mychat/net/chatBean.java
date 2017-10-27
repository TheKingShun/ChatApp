package com.luobotie.kingshun.mychat.net;

/**
 * Created by Administrator on 2017/10/24.
 */

public class chatBean {
    /**
     * status : 0
     * msg : ok
     * result : {"type":"标准回复","content":"杭州今天10℃~21℃ 晴 北风3-4 级转东北风微风\r\n建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。","relquestion":"查询天气[那么?][天气地区名|省名|城市别称][天气地区名?]"}
     */

    private String status;
    private String msg;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * type : 标准回复
         * content : 杭州今天10℃~21℃ 晴 北风3-4 级转东北风微风
         建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。
         * relquestion : 查询天气[那么?][天气地区名|省名|城市别称][天气地区名?]
         */

        private String type;
        private String content;
        private String relquestion;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRelquestion() {
            return relquestion;
        }

        public void setRelquestion(String relquestion) {
            this.relquestion = relquestion;
        }
    }
}
