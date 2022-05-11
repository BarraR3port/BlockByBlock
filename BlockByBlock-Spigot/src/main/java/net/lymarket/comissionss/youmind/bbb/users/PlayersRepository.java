package net.lymarket.comissionss.youmind.bbb.users;

import net.lymarket.comissionss.youmind.bbb.common.db.IPlayerRepository;
import net.lymarket.common.db.MongoDBClient;

public final class PlayersRepository extends IPlayerRepository {
    
    
    public PlayersRepository( MongoDBClient database , String tableName ){
        super( database , tableName );
    }
    
    
}
