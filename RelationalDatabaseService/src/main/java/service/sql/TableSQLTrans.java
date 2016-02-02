package service.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by liweiqi on 2014/12/5.
 */
public class TableSQLTrans {
    private static final TableSQLTrans trans = new TableSQLTrans();

    private final String TABLENAME = "ct_table_name";

    private final String FIELDS = "fields";
    private final String TYPE = "type";
    private final String FIELDNAME = "fieldName";
    private final String NEWNAME = "newName";
    private final String LENGTH = "length";
    private final String NULLABLE = "nullable";
    private final String PK = "pk";
    private final String UNION = "union";
    private final String FK = "fk";
    private final String INDEX = "index";
    private final String AUTOINCREMENT = "autoIncrement";
    private final String INCREMENTSTART = "incrementStart";

    public static TableSQLTrans me() {
        return trans;
    }

    private TableSQLTrans() {
    }

    /**
     * translate a json-data to create_table_sql executed in mysql
     */
    public String translateCreateSql(String jsonStr) throws SQLTansException {
        JSONObject data = JSON.parseObject(jsonStr);
        String name = data.getString(TABLENAME);
        final Map<String, TypeLimit> map = new HashMap<>();
        final StringBuilder result = new StringBuilder("CREATE TABLE `" + name + "` ( ");
        JSONArray fields = data.getJSONArray(FIELDS);
        //Normal Fields SQL
        for (int i = 0; i < fields.size(); i++) {
            JSONObject fieldJson = fields.getJSONObject(i);
            String fieldName = fieldJson.getString(FIELDNAME);
            int type = fieldJson.getInteger(TYPE);
            int length = fieldJson.getInteger(LENGTH);
            int nullable = fieldJson.getInteger(NULLABLE);
            int autoIncrement = fieldJson.getInteger(AUTOINCREMENT);
            int incrementStart = fieldJson.getInteger(INCREMENTSTART);
            if (map.containsKey(fieldName)) throw new SQLTansException("duplicate field name ");
            Field field = new Field(fieldName, type).buildLength(length).buildNullAble(nullable)
                    .buildAutoIncrement(autoIncrement).buildIncrementStart(incrementStart);
            map.put(fieldName, TypeLimit.limits.get(type));
            result.append(field.getFieldSql());
        }
        //primary key sql
        JSONArray primaryKeys = data.getJSONArray(PK);
        result.append(pkSQLBuilder(primaryKeys, map, true)).append(",");
        //add forgin key
        JSONArray foreginKeys = data.getJSONArray(FK);
        result.append(fkSQLBuilder(foreginKeys, map, true));
        // add  index
        JSONArray indexes = data.getJSONArray(INDEX);
        JSONArray unions = data.getJSONArray(UNION);
        result.append(indexSQLBuilder(indexes, unions, map, true)).setLength(result.length() - 1);
        result.append(")");
        return result.toString();
    }

    /**
     * translate a json-data to change_table_sql executed in mysql
     */
    public String translateUpdateSql(String jsonStr) throws SQLTansException {
        JSONObject data = JSON.parseObject(jsonStr);
        String name = data.getString(TABLENAME);
        final Map<String, TypeLimit> map = new HashMap<>();
        final StringBuilder result = new StringBuilder("ALTER TABLE `" + name + "` ( ");

        //fields change
        JSONArray fields = data.getJSONArray(FIELDS);
        if (fields.size() > 0) {
            for (int i = 0; i < fields.size(); i++) {
                JSONObject fieldJson = fields.getJSONObject(i);
                String fieldName = fieldJson.getString(FIELDNAME);
                String newName = fieldJson.getString(NEWNAME);
                int type = fieldJson.getInteger(TYPE);
                int length = fieldJson.getInteger(LENGTH);
                int nullable = fieldJson.getInteger(NULLABLE);
                int autoIncrement = fieldJson.getInteger(AUTOINCREMENT);
                int incrementStart = fieldJson.getInteger(INCREMENTSTART);
                if (map.containsKey(newName)) throw new SQLTansException("duplicate field name ");
                Field field = new Field(newName, type).buildLength(length).buildNullAble(nullable)
                        .buildAutoIncrement(autoIncrement).buildIncrementStart(incrementStart);
                map.put(newName, TypeLimit.limits.get(type));
                result.append("CHANGE COLUMN `" + fieldName + "` ").append(field.getFieldSql()).append(",");
            }
        }
        //primary key sql
        JSONArray primaryKeys = data.getJSONArray(PK);
        result.append(pkSQLBuilder(primaryKeys, map, false)).append(",");
        //add forgin key
        JSONArray foreginKeys = data.getJSONArray(FK);
        result.append(fkSQLBuilder(foreginKeys, map, false));
        // add  index
        JSONArray indexes = data.getJSONArray(INDEX);
        JSONArray unions = data.getJSONArray(UNION);
        result.append(indexSQLBuilder(indexes, unions, map, false)).setLength(result.length() - 1);
        result.append(")");
        return result.toString();
    }


    class Field {
        private String fieldName;
        private int type;
        private int length = 0;
        private int nullable = 0;
        private int autoIncrement = 0;
        private int incrementStart = 0;
        private TypeLimit limit;

        public Field(String fieldName, int type) {
            this.fieldName = fieldName;
            this.type = type;
            limit = TypeLimit.limits.get(type);
        }

        public Field buildAutoIncrement(int autoIncrement) {
            this.autoIncrement = autoIncrement;
            return this;
        }

        public Field buildIncrementStart(int incrementStart) {
            this.incrementStart = incrementStart;
            return this;
        }

        public Field buildLength(int length) {
            this.length = length;
            return this;
        }

        public Field buildNullAble(int nullable) {
            this.nullable = nullable;
            return this;
        }

        public String getFieldSql() {
            int maxlength = limit.getMaxLength();
            StringBuilder sb = new StringBuilder();
            sb.append("'").append(fieldName).append("' ");
            sb.append(type);
            if (maxlength > 0) {
                sb.append("(").append(length > maxlength ? maxlength : length).append(") ");
            }
            sb.append(nullable == 0 ? "NOT NULL " : "NULL ");
            if (limit.getCanAutoIncrement() == TypeLimit.CAN_AUTOINCRE && autoIncrement > 0) {
                sb.append("AUTO_INCREMENT=").append(incrementStart);
            }
            return sb.toString();
        }

    }

    //just mehods
    private String pkSQLBuilder(JSONArray primaryKeys, final Map<String, TypeLimit> map, boolean isCreated) throws SQLTansException {
        if (primaryKeys.size() == 0 && isCreated) throw new SQLTansException("must have primary key");
        if (primaryKeys.size() == 0) return "";
        final StringBuilder result = new StringBuilder();
        result.append("PRIMARY KEY(");
        primaryKeys.forEach(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                TypeLimit limit = map.get(o.toString());
                if (limit == null || limit.getCanBeIndex() == TypeLimit.CANOT_INDEX)
                    throw new SQLTansException("can't add primary key on" + o.toString());
                result.append("'").append(o.toString()).append(",");
            }
        });
        result.deleteCharAt(result.length() - 1).append(")").append(",");
        return result.toString();
    }

    private String fkSQLBuilder(JSONArray foreginKeys, final Map<String, TypeLimit> map, boolean isCreateSql) throws SQLTansException {
        if (foreginKeys.size() == 0) return "";
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < foreginKeys.size(); i++) {
            JSONArray array = foreginKeys.getJSONArray(i);
            int state = array.getInteger(0);
            String fieldName = array.getString(1);
            String fkStr = array.getString(2);
            TypeLimit limit = map.get(fieldName);
            if (state == 0 && !isCreateSql) {
                result.append("DROP FOREIGN KEY `fk_" + fieldName + "`,");
            } else if (state == 1) {
                if (limit == null || limit.getCanBeIndex() == TypeLimit.CANOT_INDEX)
                    throw new SQLTansException("can't add foreign key on" + fieldName);
                if (!isCreateSql) result.append(" ADD ");
                result.append("CONSTRAINT `fk_" + fieldName + "` FOREIGN KEY (`" + fieldName + "`) REFERENCES " + fkStr + " ON DELETE RESTRICT ON UPDATE RESTRICT")
                        .append(",");
            } else if (state == 2 && !isCreateSql) {
                result.append("DROP FOREIGN KEY `fk_" + fieldName + "`,");
                result.append("ADD CONSTRAINT `fk_" + fieldName + "` FOREIGN KEY (`" + fieldName + "`) REFERENCES " + fkStr + " ON DELETE RESTRICT ON UPDATE RESTRICT")
                        .append(",");
            }
        }
        return result.toString();
    }

    private String indexSQLBuilder(JSONArray indexes, JSONArray unions, final Map<String, TypeLimit> map, boolean isCreateSql) throws SQLTansException {
        final StringBuilder result = new StringBuilder();
        if (indexes.size() > 0) {
            for (int i = 0; i < indexes.size(); i++) {
                JSONArray array = indexes.getJSONArray(i);
                int state = array.getInteger(0);
                String fieldName = array.getString(1);
                TypeLimit limit = map.get(fieldName);
                if (state == 1) {
                    if (limit == null || limit.getCanBeIndex() == TypeLimit.CANOT_INDEX)
                        throw new SQLTansException("can't add index  on" + fieldName);
                    if (!isCreateSql) result.append("ADD ");
                    result.append("INDEX `index_" + fieldName + "` (`" + fieldName + "`) ")
                            .append(",");
                }
                if (state == 0 && !isCreateSql) {
                    result.append("DROP INDEX 'index_" + fieldName + "',");
                }
            }
        }
        if (unions.size() > 0) {
            for (int i = 0; i < unions.size(); i++) {
                JSONArray array = unions.getJSONArray(i);
                int state = array.getInteger(0);
                String fieldName = array.getString(1);
                TypeLimit limit = map.get(fieldName);
                if (state == 1) {
                    if (limit == null || limit.getCanBeIndex() == TypeLimit.CANOT_INDEX)
                        throw new SQLTansException("can't add unique index  on" + fieldName);
                    if (!isCreateSql) result.append("ADD ");
                    result.append("UNIQUE INDEX `uindex_" + fieldName + "` (`" + fieldName + "`) ")
                            .append(",");
                } else if (state == 0 && !isCreateSql) {
                    result.append("DROP INDEX 'uindex_" + fieldName + "',");
                }
            }
        }
        return result.toString();
    }


}
