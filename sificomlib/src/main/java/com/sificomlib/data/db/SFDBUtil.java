package com.sificomlib.data.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SFDBUtil {
    public static SFDBColumAnnotation getSFColumAnnoType(Field field){
        SFDBColumAnnotation sfAnnotation=null;
        ColumType columType=null;
        boolean isPrimaryKey=false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ColumType type = field.getDeclaredAnnotation(ColumType.class);
            for (EColumType currentType:type.columType()) {
                switch (currentType){
                    case PRIMARY_KEY:
                        if (!isPrimaryKey) {
                            isPrimaryKey = true;
                        }
                        break;
                    default:
                        columType = type;
                        break;
                }
            }
        } else {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation currentAnnotation : annotations) {
                if (currentAnnotation.annotationType() == ColumType.class) {
                    ColumType currentType = (ColumType) currentAnnotation;
                    if (currentType.columType()[1] == EColumType.PRIMARY_KEY) {
                        if (!isPrimaryKey) {
                            isPrimaryKey = true;
                        }
                    } else {
                        columType = currentType;
                    }
                    break;
                }
            }
        }
        if (columType!=null){
            sfAnnotation=new SFDBColumAnnotation(columType,isPrimaryKey);
        }
        return sfAnnotation;
    }

}
