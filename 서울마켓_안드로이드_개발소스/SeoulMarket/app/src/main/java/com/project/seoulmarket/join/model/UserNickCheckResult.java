package com.project.seoulmarket.join.model;

/**
 * Created by kh on 2016. 10. 27..
 */
public class UserNickCheckResult {

    public Result result;

    public class Result {
        public Message message;
    }

    public class Message {
        public String user_nickname;
    }

}
