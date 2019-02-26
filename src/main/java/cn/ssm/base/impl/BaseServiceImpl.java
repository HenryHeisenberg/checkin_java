package cn.ssm.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ssm.base.BaseService;
import tk.mybatis.mapper.common.Mapper;

public class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	protected Mapper<T> mapper;

	@Override
	public Mapper<T> getMapper() {
		return mapper;
	}

	@Override
	public int insert(T entity) {
		return mapper.insert(entity);
	}

	@Override
	public int insertSelective(T entity) {
		return mapper.insertSelective(entity);
	}

	@Override
	public int delete(T entity) {
		return mapper.delete(entity);
	}

	@Override
	public int deleteByExample(Object example) {
		return mapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	public List<T> select(T entity) {
		return mapper.select(entity);
	}

	@Override
	public List<T> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public T selectByKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public List<T> selectByExample(Object example) {
		return mapper.selectByExample(example);
	}

	@Override
	public List<T> selectByExampleAndRowBounds(T entity, RowBounds rowBounds) {
		return mapper.selectByExampleAndRowBounds(entity, rowBounds);
	}

	@Override
	public List<T> selectByRowBounds(T entity, RowBounds rowBounds) {
		return mapper.selectByRowBounds(entity, rowBounds);
	}

	@Override
	public int selectCount(T entity) {
		return mapper.selectCount(entity);
	}

	@Override
	public int selectCountByExample(Object example) {
		return mapper.selectCountByExample(example);
	}

	@Override
	public T selectOne(T entity) {
		return mapper.selectOne(entity);
	}

	@Override
	public T selectOneByExample(Object example) {
		return mapper.selectOneByExample(example);
	}

	@Override
	public int updateByExample(T entity, Object example) {
		return mapper.updateByExample(entity, example);
	}

	@Override
	public int updateByExampleSelective(T entity, Object example) {
		return mapper.updateByExampleSelective(entity, example);
	}

	@Override
	public int updateByPrimaryKey(T entity) {
		return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateByPrimaryKeySelective(T entity) {
		return mapper.updateByPrimaryKeySelective(entity);
	}

}
