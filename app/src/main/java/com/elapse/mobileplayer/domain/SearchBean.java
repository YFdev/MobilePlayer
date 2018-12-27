package com.elapse.mobileplayer.domain;

import java.util.List;

/**
 * 新闻bean
 * Created by YF_lala on 2018/12/23.
 */

public class SearchBean {


    /**
     * result : [{"title":"特朗普携第一夫人突访伊拉克 系上任后首访作战区域","content":"当天,<em>特朗普<\/em>在伊拉克巴格达西部的阿萨德空军基地发表讲话,为美国从叙利亚撤军的决定进行辩解,称击败伊斯兰武装分子使这一决定成为可能。&quot;我们在叙利亚的部署不是无止境的,从来没有打算永久驻军。&quot;他说。<em>特朗普<\/em>在伊拉克停留了三个多小时,此后还将视察德国拉姆施泰...","img_width":"","full_title":"特朗普携第一夫人突访伊拉克 系上任后首访作战区域","pdate":"11分钟前","src":"新京报网","img_length":"","img":"","url":null,"pdate_src":"2018-12-27 12:16:43"},{"title":"特朗普突访伊拉克,与美国士兵握手、合照、发表讲话","content":"据新华社电 伊拉克国家电视台报道,美国总统<em>特朗普<\/em>夫妇乘坐&quot;空军一号&quot;于26日晨抵达位于伊拉克西部安巴尔省的阿萨德空军基地,他们在基地餐厅会见了驻伊美军官兵。这是<em>特朗普<\/em>上任近两年来首度到访伊拉克,有关行程事前并未公布。 报道说,此次访问持续数小时,其间<em>特朗<\/em>...","img_width":"683","full_title":"特朗普突访伊拉克,与美国士兵握手、合照、发表讲话","pdate":"26分钟前","src":"新浪","img_length":"1024","img":"http://p2.qhimg.com/t017cedd91d058815a3.jpg","url":null,"pdate_src":"2018-12-27 12:01:50"},{"title":"特朗普突访驻伊美军 系其就任以来首访作战区域","content":"据悉,此访系<em>特朗普<\/em>中东政策调整之际。早前他宣布将从叙利亚撤军,而伊拉克表示此举将会有重大威胁。自2003年对伊行动15年以来,美国仍然有超过5000名驻伊拉克军队赞襄伊政府,以打击极端势力。对<em>特朗普<\/em>撤军,伊拉克总理阿卜杜勒-马赫迪最近表示,伊拉克军队或部署到叙利亚,以保...","img_width":"337","full_title":"特朗普突访驻伊美军 系其就任以来首访作战区域","pdate":"26分钟前","src":"网易军事","img_length":"600","img":"http://p1.qhimg.com/t015aac2f0ba9a0a967.jpg","url":null,"pdate_src":"2018-12-27 12:01:10"},{"title":"突访伊拉克 特朗普透露重要信息","content":"总统专机&quot;空军一号&quot;连夜从华盛顿出发,26日晚抵达伊拉克首都巴格达以西的阿萨德空军基地。<em>特朗普<\/em>抵达后为早前的撤军决定辩护,但强调无意撤走驻伊的5000名美军。他说:&quot;我想很多人都会同意我的想法。是时候开始动脑筋了。事实上,如果想在叙利亚做点什么,我们...","img_width":"299","full_title":"突访伊拉克 特朗普透露重要信息","pdate":"20分钟前","src":"新浪","img_length":"550","img":"http://p8.qhimg.com/t01acb96fa6d1cb6b4d.jpg","url":null,"pdate_src":"2018-12-27 12:07:49"}]
     * error_code : 0
     * reason : Succes
     */

    private int error_code;
    private String reason;
    private List<ResultBean> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * title : 特朗普携第一夫人突访伊拉克 系上任后首访作战区域
         * content : 当天,<em>特朗普</em>在伊拉克巴格达西部的阿萨德空军基地发表讲话,为美国从叙利亚撤军的决定进行辩解,称击败伊斯兰武装分子使这一决定成为可能。&quot;我们在叙利亚的部署不是无止境的,从来没有打算永久驻军。&quot;他说。<em>特朗普</em>在伊拉克停留了三个多小时,此后还将视察德国拉姆施泰...
         * img_width :
         * full_title : 特朗普携第一夫人突访伊拉克 系上任后首访作战区域
         * pdate : 11分钟前
         * src : 新京报网
         * img_length :
         * img :
         * url : null
         * pdate_src : 2018-12-27 12:16:43
         */

        private String title;
        private String content;
        private String img_width;
        private String full_title;
        private String pdate;
        private String src;
        private String img_length;
        private String img;
        private Object url;
        private String pdate_src;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg_width() {
            return img_width;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        public String getFull_title() {
            return full_title;
        }

        public void setFull_title(String full_title) {
            this.full_title = full_title;
        }

        public String getPdate() {
            return pdate;
        }

        public void setPdate(String pdate) {
            this.pdate = pdate;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getImg_length() {
            return img_length;
        }

        public void setImg_length(String img_length) {
            this.img_length = img_length;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public String getPdate_src() {
            return pdate_src;
        }

        public void setPdate_src(String pdate_src) {
            this.pdate_src = pdate_src;
        }
    }
}
