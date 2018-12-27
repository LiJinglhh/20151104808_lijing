package com.dao;
import com.model.VoteBatch;
import com.model.VoteBatchExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteBatchMapper {
    long countByExample(VoteBatchExample example);

    int deleteByExample(VoteBatchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VoteBatch record);

    int insertSelective(VoteBatch record);

    List<VoteBatch> selectByExample(VoteBatchExample example);

    VoteBatch selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VoteBatch record, @Param("example") VoteBatchExample example);

    int updateByExample(@Param("record") VoteBatch record, @Param("example") VoteBatchExample example);

    int updateEndTimeByPrimaryKeySelective(VoteBatch record);

    int updateByPrimaryKey(VoteBatch record);

    VoteBatch selectByBatchName(String batchName);

    List<VoteBatch> selectLikeBatchName(@Param("batchName") String batchName);
}