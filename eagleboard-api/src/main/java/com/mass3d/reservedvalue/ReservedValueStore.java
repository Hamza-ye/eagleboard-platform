package com.mass3d.reservedvalue;

import java.util.List;
import com.mass3d.common.GenericStore;

public interface ReservedValueStore
    extends GenericStore<ReservedValue>
{
    List<ReservedValue> reserveValues(ReservedValue reservedValue, List<String> values);

    List<ReservedValue> reserveValuesJpa(ReservedValue reservedValue, List<String> values);

    List<ReservedValue> getIfReservedValues(ReservedValue reservedValue, List<String> values);

    int getNumberOfUsedValues(ReservedValue reservedValue);

    void removeExpiredReservations();

    boolean useReservedValue(String ownerUID, String value);

    void deleteReservedValueByUid(String uid);

    boolean isReserved(String ownerObject, String ownerUID, String value);
}
