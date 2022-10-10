package com.microel.speedtest.repositories.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import com.microel.speedtest.common.types.SpeedChartPoint;

public class SpeedChartPointType implements UserType {

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value == null ? null : value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);
        if (!(deepCopy instanceof Serializable)) {
            return (Serializable) deepCopy;
        }
        return null;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return (x == y) || (x != null && y != null && x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] value, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        if (rs.wasNull()) {
            return null;
        }
        return new SpeedChartPoint(rs.getInt("stamp"), rs.getFloat("speed"));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            SpeedChartPoint particle = (SpeedChartPoint) value;
            st.setInt(index, particle.getStamp());
            st.setFloat(index + 1, particle.getSpeed());
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public Class<SpeedChartPoint> returnedClass() {
        return SpeedChartPoint.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.INTEGER, Types.FLOAT };
    }

}
