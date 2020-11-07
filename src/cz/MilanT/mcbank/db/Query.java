package cz.MilanT.mcbank.db;

/* http://www.itnetwork.cz */

public class Query {
    private StringBuilder sql;

    public Query delete(String table){
        sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(table);
        return this;
    }

    public Query where(String requirement){
        sql.append(" WHERE ");
        sql.append(requirement);
        return this;
    }

    public Query from(String table){
        sql.append(" FROM ");
        sql.append(table);
        return this;
    }

    public Query update(String table){
        sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(table);
        sql.append(" SET ");
        return this;
    }

    public Query set(String[] columns){
        int count = columns.length;
        if(count == 0)
            throw new IllegalArgumentException("Invalid argument count");

        for(String column : columns){
            sql.append(column);
            sql.append(" = ");
            sql.append("?");
            sql.append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        return this;
    }

    public Query insert(String table){
        sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(table);
        return this;
    }

    public Query values(Object[] params){
        sql.append(" VALUES(");

        int count = params.length;

        if(count == 0)
            throw new IllegalArgumentException("Invalid parameter count");

        for (int i = 0; i<count; i++) {
            sql.append("?,");
        }
        //removes the last comma
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(");");
        return this;
    }

    public Query select(Object[] columns){
        sql = new StringBuilder();
        sql.append("SELECT ");
        if(columns != null){
            for(Object column : columns){
                sql.append(column);
                sql.append(",");
            }
            //removes the last question mark
            sql.deleteCharAt(sql.lastIndexOf(","));
        }
        else
            sql.append("*");

        return this;
    }

    public String getQuery(){
        return sql.toString();
    }
}