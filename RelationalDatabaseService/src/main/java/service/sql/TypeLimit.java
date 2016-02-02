package service.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2014/12/8.
 */
public class TypeLimit {
    public static int CAN_INDEX = 1;
    public static int CANOT_INDEX = 0;
    public static int CAN_AUTOINCRE = 1;
    public static int CANOT_AUTOINCRE = 0;
    public static Map<Integer, TypeLimit> limits = new HashMap<>(40);

    static {
        //num
        limits.put(1, new TypeLimit(1, "tinyint", 255, CAN_INDEX, CAN_AUTOINCRE));
        limits.put(2, new TypeLimit(2, "smallint", 255, CAN_INDEX, CAN_AUTOINCRE));
        limits.put(3, new TypeLimit(3, "mediumint", 255, CAN_INDEX, CAN_AUTOINCRE));
        limits.put(4, new TypeLimit(4, "int", 255, CAN_INDEX, CAN_AUTOINCRE));
        limits.put(5, new TypeLimit(5, "bigint", 255, CAN_INDEX, CAN_AUTOINCRE));
        limits.put(6, new TypeLimit(6, "bit", 64, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(7, new TypeLimit(7, "double", 255, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(8, new TypeLimit(8, "float", 255, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(9, new TypeLimit(9, "decimal", 255, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(10, new TypeLimit(10, "numeric", 255, CAN_INDEX, CANOT_AUTOINCRE));
        //char
        limits.put(11, new TypeLimit(11, "char", 255, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(12, new TypeLimit(12, "varchar", 255, CAN_INDEX, CANOT_AUTOINCRE));
        //time
        limits.put(13, new TypeLimit(13, "time", 0, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(14, new TypeLimit(14, "date", 0, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(15, new TypeLimit(15, "year", 0, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(16, new TypeLimit(16, "timestamp", 0, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(17, new TypeLimit(17, "datetime", 0, CAN_INDEX, CANOT_AUTOINCRE));
        //blob&&text
        limits.put(18, new TypeLimit(18, "tinyblob", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(19, new TypeLimit(19, "blob", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(20, new TypeLimit(20, "mediumblob", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(21, new TypeLimit(21, "longblob", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(22, new TypeLimit(22, "tinytext", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(23, new TypeLimit(23, "text", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(24, new TypeLimit(24, "mediumtext", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(25, new TypeLimit(25, "longtext", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        //enum&&set
        limits.put(26, new TypeLimit(26, "enum", 0, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(27, new TypeLimit(27, "set", 0, CAN_INDEX, CANOT_AUTOINCRE));
        //binary&&varbinary
        limits.put(28, new TypeLimit(28, "binary", 255, CAN_INDEX, CANOT_AUTOINCRE));
        limits.put(29, new TypeLimit(29, "varbinary", 255, CAN_INDEX, CANOT_AUTOINCRE));
        //graphic
        limits.put(30, new TypeLimit(30, "point", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(31, new TypeLimit(31, "linestring", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(32, new TypeLimit(32, "polygon", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(33, new TypeLimit(33, "geometry", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(34, new TypeLimit(34, "multipoint", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(35, new TypeLimit(35, "multilinestring", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(36, new TypeLimit(36, "multipolygon", 0, CANOT_INDEX, CANOT_AUTOINCRE));
        limits.put(37, new TypeLimit(37, "geometrycollection", 0, CANOT_INDEX, CANOT_AUTOINCRE));

    }

    private int typeCode;
    private String typeName;
    private int maxLength;
    private int canBeIndex;
    private int canAutoIncrement;

    public TypeLimit(int typeCode, String typeName, int maxLength, int canBeIndex, int canAutoIncrement) {
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.maxLength = maxLength;
        this.canBeIndex = canBeIndex;
        this.canAutoIncrement = canAutoIncrement;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof TypeLimit) {
            if (this.getTypeCode() == ((TypeLimit) obj).getTypeCode() || this.getTypeName().equals(((TypeLimit) obj).getTypeName())) {
                return true;
            }
        }
        return false;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getCanBeIndex() {
        return canBeIndex;
    }

    public void setCanBeIndex(int canBeIndex) {
        this.canBeIndex = canBeIndex;
    }

    public int getCanAutoIncrement() {
        return canAutoIncrement;
    }

    public void setCanAutoIncrement(int canAutoIncrement) {
        this.canAutoIncrement = canAutoIncrement;
    }
}
