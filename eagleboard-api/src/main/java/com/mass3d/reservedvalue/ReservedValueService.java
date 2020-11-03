package com.mass3d.reservedvalue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.mass3d.textpattern.TextPattern;
import com.mass3d.textpattern.TextPatternGenerationException;

public interface ReservedValueService
{
    List<ReservedValue> reserve(TextPattern textPattern, int numberOfReservations,
        Map<String, String> values, Date expires)
        throws ReserveValueException, TextPatternGenerationException;

    boolean useReservedValue(TextPattern textPattern, String value);

    boolean isReserved(TextPattern textPattern, String value);

    void removeExpiredReservations();

    void deleteReservedValueByUid(String uid);
}
