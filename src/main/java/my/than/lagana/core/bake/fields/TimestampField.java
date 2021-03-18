package my.than.lagana.core.bake.fields;


import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.List;

public class TimestampField extends NumericField {

    //private static Long BUCKET_SIZE = 1000 * 60 * 5L ;//milliseconds

    public TimestampField(String name, String desc) {
        super(name, desc, GenericFieldTypeEnum.TIMESTAMP, false, true);
    }



    //    @Override
//    public void put(Long id, Long value) {
//        for(Long key : this.indexedItems.keySet()){
//            if(this.isGT(value,key) && this.isLT(value,key+BUCKET_SIZE)){
//                value = key;
//            }
//        }
//        super.put(id, value);
//    }
//
//    @Override
//    protected Set<Long> filterValueInd(Long filter, OperatorEnum op) {
//        Set<Long> result = new HashSet<>();
//
//        for (Long key : this.indexedItems.keySet()) {
//            try {
//                if (filterValue(key, filter, OperatorEnum.GT) && filterValue(key + BUCKET_SIZE, filter, OperatorEnum.LT)) {
//                    result.addAll(this.indexedItems.get(key));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
}
