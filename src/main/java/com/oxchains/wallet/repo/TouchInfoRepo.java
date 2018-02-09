package com.oxchains.wallet.repo;

import com.oxchains.wallet.entity.TouchInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2018/1/29.
 */
@Repository
public interface TouchInfoRepo extends MongoRepository<TouchInfo,Long>{

    TouchInfo findByAddress(String address);

}
