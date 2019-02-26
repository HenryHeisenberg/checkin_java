package cn.ssm.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import tk.mybatis.mapper.common.Mapper;

public interface BaseService<T> {

	public Mapper<T> getMapper();

	public int insert(T entity);

	public int insertSelective(T entity);

	public int delete(T entity);

	public int deleteByExample(Object example);

	public int deleteByPrimaryKey(Object key);

	public List<T> select(T entity);

	public List<T> selectAll();

	public T selectByKey(Object key);

	public List<T> selectByExample(Object example);

	public List<T> selectByExampleAndRowBounds(T entity, RowBounds rowBounds);

	public List<T> selectByRowBounds(T entity, RowBounds rowBounds);

	public int selectCount(T entity);

	public int selectCountByExample(Object example);

	public T selectOne(T entity);

	public T selectOneByExample(Object example);

	public int updateByExample(T entity, Object example);

	public int updateByExampleSelective(T entity, Object example);

	public int updateByPrimaryKey(T entity);

	public int updateByPrimaryKeySelective(T entity);
}
