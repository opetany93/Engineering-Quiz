package net.ddns.opetany.engineeringquiz;

import com.orm.SugarRecord;

/**
 * Created by Łysol on 2016-05-22.
 */
public class RankingClass extends SugarRecord {
    String login;
    String lvl;

    public RankingClass(){
    }

    public RankingClass(String login, String lvl){
        this.login = login;
        this.lvl = lvl;
    }
}
