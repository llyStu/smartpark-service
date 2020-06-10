package com.vibe.mapper.emergency;

import com.vibe.pojo.emergency.Operation;
import com.vibe.pojo.emergency.OperationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationMapper {

	int insertOperation(Operation oper);

	int deleteOperation(@Param("oids") int[] oids);

	int updateOperation(Operation oper);

	List<Operation> queryOperation(OperationVo oper);

}
