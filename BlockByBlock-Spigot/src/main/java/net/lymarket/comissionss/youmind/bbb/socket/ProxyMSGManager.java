/*
 * BedWars1058 - A bed wars mini-game.
 * Copyright (C) 2021 Andrei Dascălu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: andrew.dascalu@gmail.com
 */

package net.lymarket.comissionss.youmind.bbb.socket;

import net.lymarket.comissionss.youmind.bbb.Main;
import net.lymarket.comissionss.youmind.bbb.event.PrevCreateWorld;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ProxyMSGManager implements Listener {
    
    
    /**
     * Enviar el mensaje al proxy para que se organice la creación del mundo
     */
    @EventHandler
    public void onPrevCreateWorld( PrevCreateWorld e ){
        if ( e == null ) return;
        if ( e.isCancelled( ) ) return;
        Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) ,
                ( ) -> Main.getInstance( ).getSocket( ).sendFormattedCreateWorldMSG( e ) );
    }
    
    
    
    /*@EventHandler
    public void onPlayerLeaveArena( PlayerLeaveArenaEvent e ){
        if ( e == null ) return;
        final IArena a = e.getArena( );
        Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) , ( ) -> ArenaSocket.sendMessage( ArenaSocket.formatCreateWorldMSG( a ) ) );
    }
    
    @EventHandler
    public void onArenaStatusChange( GameStateChangeEvent e ){
        if ( e == null ) return;
        final IArena a = e.getArena( );
        Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) , ( ) -> ArenaSocket.sendMessage( ArenaSocket.formatCreateWorldMSG( a ) ) );
    }
    
    @EventHandler
    public void onArenaLoad( ArenaEnableEvent e ){
        if ( e == null ) return;
        final IArena a = e.getArena( );
        Bukkit.getScheduler( ).runTaskAsynchronously( Main.getInstance( ) , ( ) -> ArenaSocket.sendMessage( ArenaSocket.formatCreateWorldMSG( a ) ) );
    }*/
}
