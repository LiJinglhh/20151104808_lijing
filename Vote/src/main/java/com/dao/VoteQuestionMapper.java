package com.dao;
import com.model.VoteQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteQuestionMapper {

    int insert(VoteQuestion record);

    List<VoteQuestion> selectByBatchId(Integer questionBatchId);

    int deleteByBatchId(Integer questionBatchId);

    VoteQuestion selectByQuestionId(Integer questionId);

    int updateCheckNumber(VoteQuestion record);
}