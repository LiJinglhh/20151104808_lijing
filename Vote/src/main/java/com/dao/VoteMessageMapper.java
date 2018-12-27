package com.dao;
import com.model.VoteMessage;
import com.model.VoteQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteMessageMapper {

    int insert(VoteMessage record);

    VoteMessage select(@Param("userName") String userName, @Param("batchId")Integer batchId);

}