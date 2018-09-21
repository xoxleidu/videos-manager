package com.dulei.pojo.redis;

import java.util.Date;

public class CommentsVO {
    private String id;
    private String videoId;
    /**
     * 针对哪条留言的ID 相当于commentsID
     */
    private String fatherCommentId;
    /**
     * 回复谁的ID 回复谁就是谁的ID
     */
    private String toUserId;
    /**
     * 留言者的ID 谁留言就是谁的ID，(当前用户ID)
     */
    private String fromUserId;
    private Date createTime;
    private String comment;

    private String faceImage;
    private String nickname;
    private String toNickname;
    private String timeAgoStr;

    public String getFatherCommentId() {
        return fatherCommentId;
    }

    public CommentsVO setFatherCommentId(String fatherCommentId) {
        this.fatherCommentId = fatherCommentId;
        return this;
    }

    public String getToUserId() {
        return toUserId;
    }

    public CommentsVO setToUserId(String toUserId) {
        this.toUserId = toUserId;
        return this;
    }

    public String getId() {
        return id;
    }

    public CommentsVO setId(String id) {
        this.id = id;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }

    public CommentsVO setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public CommentsVO setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public CommentsVO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CommentsVO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public CommentsVO setFaceImage(String faceImage) {
        this.faceImage = faceImage;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public CommentsVO setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getToNickname() {
        return toNickname;
    }

    public CommentsVO setToNickname(String toNickname) {
        this.toNickname = toNickname;
        return this;
    }

    public String getTimeAgoStr() {
        return timeAgoStr;
    }

    public CommentsVO setTimeAgoStr(String timeAgoStr) {
        this.timeAgoStr = timeAgoStr;
        return this;
    }
}