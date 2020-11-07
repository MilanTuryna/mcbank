package cz.MilanT.mcbank.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* http://www.itnetwork.cz */

public class Database {

     protected final Connection connection;
     protected Query query;

     public Database(String db, String userName, String password) throws SQLException{
          this.connection = DriverManager.getConnection("jdbc:mysql://localhost/" + db, userName, password);
     }

    private int query(String query, Object[] params) throws SQLException{
         PreparedStatement ps = connection.prepareStatement(query);
         if (params != null){
           int index = 1;
           for(Object param : params){
             ps.setObject(index, param);
            index++;
           }
         }
         return ps.executeUpdate();
    }

    public int delete(String table, String requirement, Object[] param) throws SQLException{
        query = new Query();
        query.delete(table)
             .where(requirement);
        return query(query.getQuery(), param);
    }

    public int insert(String table, Object[] params) throws SQLException{
        query = new Query();
        query.insert(table)
             .values(params);
        return query(query.getQuery(), params);
    }

    public int update(String table, String[] columns, String requirement, Object[] params) throws SQLException{
        query = new Query();

        query.update(table)
             .set(columns)
             .where(requirement);

        return query(query.getQuery(), params);
    }

    public ResultSet select(String table, Object[] columns, Object[] params) throws SQLException{
        return this.select(table, columns, "", params);
    }

    public ResultSet select(String table, Object[] columns, String requirement, Object[] params) throws SQLException {
        query = new Query();
        query.select(columns)
                .from(table)
                .where(requirement);

        PreparedStatement ps = connection.prepareStatement(query.getQuery());
        if (params != null) {
            int index = 1;
            for (Object param : params) {
                ps.setObject(index, param);
                index++;
            }
        }

        return ps.executeQuery();
    }

    public int count(String table) throws SQLException{
        PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(*) FROM "+table);
       ResultSet result = ps.executeQuery();
         result.next();
        return result.getInt(1);
    }
}