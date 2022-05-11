package net.lymarket.comissionss.youmind.bbb.velocity.manager;

import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.common.db.MongoDBClient;

public class PlayersRepository extends IPlayerRepository {
    public PlayersRepository( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    
}
