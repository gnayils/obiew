package com.gnayils.obiew.weibo.bean;

import java.util.List;

/**
 * Created by Gnayils on 18/03/2017.
 */

public class RepostTimeline {

    public boolean hasvisible;
    public long previous_cursor;
    public long next_cursor;
    public int total_number;
    public long since_id;
    public long max_id;
    public Status status;
    public List<Repost> reposts;
    public List<?> marks;
}