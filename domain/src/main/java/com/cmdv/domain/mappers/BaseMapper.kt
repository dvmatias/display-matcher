package com.cmdv.domain.mappers

abstract class BaseMapper<E, M> {

    open fun transformEntityToModel(e: E): M {
        throw UnsupportedOperationException()
    }

    open fun transformModelToEntity(m: M): E {
        throw UnsupportedOperationException()
    }

}