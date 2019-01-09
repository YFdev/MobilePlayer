package com.elapse.mobileplayer.domain;

import java.util.List;

/**
 * 网络音乐数据
 * Created by YF_lala on 2018/12/30.
 */

public class NetAudioPagerBean {

    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * count : 4175
         * np : 1546145342
         */

        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {
        /**
         * status : 4
         * comment : 2
         * cate : 囧事神曲
         * tags : [{"post_number":489711,"image_list":"http://img.spriteapp.cn/ugc/2017/07/8225f4ee660711e7978d842b2b4c75ab.png","forum_sort":0,"forum_status":2,"id":58191,"info":"爆笑视频让你笑的飞起\r\n\r\n乱选分类会被版主删帖哦~\r\n\r\n版主申请加微信：L1391446139","name":"搞笑视频","colum_set":1,"tail":"个小逗比","sub_number":58797,"display_level":0}]
         * bookmark : 5
         * text : 哈哈哈  你们想气死这孩子啊  快给他吧
         * is_best : 0
         * up : 87
         * share_url : http://a.f.budejie.com/share/29062380.html?wx.qq.com
         * down : 2
         * forward : 7
         * u : {"header":["http://wimg.spriteapp.cn/profile/large/2018/08/06/5b6742613dbe7_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/08/06/5b6742613dbe7_mini.jpg"],"relationship":0,"uid":"19634336","is_vip":false,"is_v":false,"room_url":"","room_name":"OMEGA帮派","room_role":"帮主","room_icon":"http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_4.png","name":"全球领先在线视频 [OMEGA帮派]"}
         * passtime : 2018-12-30 15:57:02
         * video : {"playfcount":26,"height":640,"width":352,"video":["http://wvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpd.mp4","http://tvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpd.mp4"],"download":["http://wvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpdm.mp4","http://tvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpdm.mp4"],"duration":78,"playcount":888,"thumbnail":["http://wimg.spriteapp.cn/picture/2018/1229/29062380_475.jpg","http://dimg.spriteapp.cn/picture/2018/1229/29062380_475.jpg"],"thumbnail_small":["http://wimg.spriteapp.cn/crop/150x150/picture/2018/1229/29062380_475.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2018/1229/29062380_475.jpg"]}
         * type : video
         * id : 29062380
         * top_comments : [{"voicetime":0,"status":0,"hate_count":0,"cmt_type":"text","precid":0,"content":"很现实，喜欢这种气氛的家庭","like_count":30,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg"],"uid":"21823670","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"为人处世i"},"preuid":0,"passtime":"2018-12-30 11:53:33","voiceuri":"","id":11422185},{"voicetime":0,"status":2,"hate_count":0,"cmt_type":"text","precid":0,"content":"晚上多用用功","like_count":6,"u":{"header":["http://wx.qlogo.cn/mmopen/wjVtTPhRGG80PyG4gZqJSVo4tt0KbyIvhwG8qoKlIj07A4tXFO1tzDS8icCMDV9VSkMQgjmQJ1wVRkxO4R5a13Q/0","http://wx.qlogo.cn/mmopen/wjVtTPhRGG80PyG4gZqJSVo4tt0KbyIvhwG8qoKlIj07A4tXFO1tzDS8icCMDV9VSkMQgjmQJ1wVRkxO4R5a13Q/0"],"uid":"20352741","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"炙热狂想曲Yu4"},"preuid":0,"passtime":"2018-12-30 15:40:53","voiceuri":"","id":11427432}]
         * image : {"medium":[],"big":["http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_d.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_d.jpg","http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847.jpg"],"height":1637,"width":550,"small":[],"thumbnail_small":["http://wimg.spriteapp.cn/crop/150x150/ugc/2018/12/30/5c282f8872847.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2018/12/30/5c282f8872847.jpg"]}
         * gif : {"images":["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7.gif","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7.gif"],"width":270,"gif_thumbnail":["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_d.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_d.jpg","http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg"],"height":138}
         */

        private int status;
        private String comment;
        private String cate;
        private String bookmark;
        private String text;
        private int is_best;
        private String up;
        private String share_url;
        private int down;
        private int forward;
        private UBean u;
        private String passtime;
        private VideoBean video;
        private String type;
        private String id;
        private ImageBean image;
        private GifBean gif;
        private List<TagsBean> tags;
        private List<TopCommentsBean> top_comments;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getIs_best() {
            return is_best;
        }

        public void setIs_best(int is_best) {
            this.is_best = is_best;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public GifBean getGif() {
            return gif;
        }

        public void setGif(GifBean gif) {
            this.gif = gif;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<TopCommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<TopCommentsBean> top_comments) {
            this.top_comments = top_comments;
        }

        public static class UBean {
            /**
             * header : ["http://wimg.spriteapp.cn/profile/large/2018/08/06/5b6742613dbe7_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/08/06/5b6742613dbe7_mini.jpg"]
             * relationship : 0
             * uid : 19634336
             * is_vip : false
             * is_v : false
             * room_url :
             * room_name : OMEGA帮派
             * room_role : 帮主
             * room_icon : http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_4.png
             * name : 全球领先在线视频 [OMEGA帮派]
             */

            private int relationship;
            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

            public int getRelationship() {
                return relationship;
            }

            public void setRelationship(int relationship) {
                this.relationship = relationship;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }

        public static class VideoBean {
            /**
             * playfcount : 26
             * height : 640
             * width : 352
             * video : ["http://wvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpd.mp4","http://tvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpd.mp4"]
             * download : ["http://wvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpdm.mp4","http://tvideo.spriteapp.cn/video/2018/1229/5c2760d7410fd_wpdm.mp4"]
             * duration : 78
             * playcount : 888
             * thumbnail : ["http://wimg.spriteapp.cn/picture/2018/1229/29062380_475.jpg","http://dimg.spriteapp.cn/picture/2018/1229/29062380_475.jpg"]
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2018/1229/29062380_475.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2018/1229/29062380_475.jpg"]
             */

            private int playfcount;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> video;
            private List<String> download;
            private List<String> thumbnail;
            private List<String> thumbnail_small;

            public int getPlayfcount() {
                return playfcount;
            }

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public List<String> getVideo() {
                return video;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public List<String> getDownload() {
                return download;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class ImageBean {
            /**
             * medium : []
             * big : ["http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_d.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847_d.jpg","http://wimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c282f8872847.jpg"]
             * height : 1637
             * width : 550
             * small : []
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2018/12/30/5c282f8872847.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2018/12/30/5c282f8872847.jpg"]
             */

            private int height;
            private int width;
            private List<?> medium;
            private List<String> big;
            private List<String> download_url;
            private List<?> small;
            private List<String> thumbnail_small;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<?> getMedium() {
                return medium;
            }

            public void setMedium(List<?> medium) {
                this.medium = medium;
            }

            public List<String> getBig() {
                return big;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<?> getSmall() {
                return small;
            }

            public void setSmall(List<?> small) {
                this.small = small;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class GifBean {
            /**
             * images : ["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7.gif","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7.gif"]
             * width : 270
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_d.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_d.jpg","http://wimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg","http://dimg.spriteapp.cn/ugc/2018/12/30/5c280989ca4f7_a_1.jpg"]
             * height : 138
             */

            private int width;
            private int height;
            private List<String> images;
            private List<String> gif_thumbnail;
            private List<String> download_url;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }
        }

        public static class TagsBean {
            /**
             * post_number : 489711
             * image_list : http://img.spriteapp.cn/ugc/2017/07/8225f4ee660711e7978d842b2b4c75ab.png
             * forum_sort : 0
             * forum_status : 2
             * id : 58191
             * info : 爆笑视频让你笑的飞起

             乱选分类会被版主删帖哦~

             版主申请加微信：L1391446139
             * name : 搞笑视频
             * colum_set : 1
             * tail : 个小逗比
             * sub_number : 58797
             * display_level : 0
             */

            private int post_number;
            private String image_list;
            private int forum_sort;
            private int forum_status;
            private int id;
            private String info;
            private String name;
            private int colum_set;
            private String tail;
            private int sub_number;
            private int display_level;

            public int getPost_number() {
                return post_number;
            }

            public void setPost_number(int post_number) {
                this.post_number = post_number;
            }

            public String getImage_list() {
                return image_list;
            }

            public void setImage_list(String image_list) {
                this.image_list = image_list;
            }

            public int getForum_sort() {
                return forum_sort;
            }

            public void setForum_sort(int forum_sort) {
                this.forum_sort = forum_sort;
            }

            public int getForum_status() {
                return forum_status;
            }

            public void setForum_status(int forum_status) {
                this.forum_status = forum_status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getColum_set() {
                return colum_set;
            }

            public void setColum_set(int colum_set) {
                this.colum_set = colum_set;
            }

            public String getTail() {
                return tail;
            }

            public void setTail(String tail) {
                this.tail = tail;
            }

            public int getSub_number() {
                return sub_number;
            }

            public void setSub_number(int sub_number) {
                this.sub_number = sub_number;
            }

            public int getDisplay_level() {
                return display_level;
            }

            public void setDisplay_level(int display_level) {
                this.display_level = display_level;
            }
        }

        public static class TopCommentsBean {
            /**
             * voicetime : 0
             * status : 0
             * hate_count : 0
             * cmt_type : text
             * precid : 0
             * content : 很现实，喜欢这种气氛的家庭
             * like_count : 30
             * u : {"header":["http://wimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg"],"uid":"21823670","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"为人处世i"}
             * preuid : 0
             * passtime : 2018-12-30 11:53:33
             * voiceuri :
             * id : 11422185
             */

            private int voicetime;
            private int status;
            private int hate_count;
            private String cmt_type;
            private int precid;
            private String content;
            private int like_count;
            private UBeanX u;
            private int preuid;
            private String passtime;
            private String voiceuri;
            private int id;

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getHate_count() {
                return hate_count;
            }

            public void setHate_count(int hate_count) {
                this.hate_count = hate_count;
            }

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public UBeanX getU() {
                return u;
            }

            public void setU(UBeanX u) {
                this.u = u;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class UBeanX {
                /**
                 * header : ["http://wimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg","http://dimg.spriteapp.cn/profile/large/2018/05/07/5aefef7f41360_mini.jpg"]
                 * uid : 21823670
                 * is_vip : false
                 * room_url :
                 * sex : m
                 * room_name :
                 * room_role :
                 * room_icon :
                 * name : 为人处世i
                 */

                private String uid;
                private boolean is_vip;
                private String room_url;
                private String sex;
                private String room_name;
                private String room_role;
                private String room_icon;
                private String name;
                private List<String> header;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }
    }
}
