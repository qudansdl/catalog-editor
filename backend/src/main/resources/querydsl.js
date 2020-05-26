if (!Object.prototype.getClassName) {
    Object.prototype.getClassName = function () {
        return Object.prototype.toString.call(this).match(/^\[object\s(.*)\]$/)[1];
    }
}

function findAll()
{
    let qClassInstance = context.getQClassInstance()
    let recordsTotal = context.getQuery().select(context.getQClassInstance().id.countDistinct()).fetchOne()

    if(context.getInput())
        for (column of context.getInput().getColumns()) {
            addCondition(column, qClassInstance)
        }

    context.getQuery().where(context.getWhere())

    let totalCount = context.getQuery().select(context.getQClassInstance().id.countDistinct()).fetchOne()

    let list
    if(context.getInput())
    {
        context.getQuery().orderBy()
        list = context.getQuery().select(context.getQClassInstance()).offset(context.getInput().getStart()).limit(context.getInput().getLength()).fetch()
    }else{
        list = context.getQuery().select(context.getQClassInstance()).fetch()
    }

    context.getOutput().setRecordsTotal(recordsTotal)
    context.getOutput().setRecordsFiltered(totalCount)
    context.getOutput().setData(list)
    console.log(recordsTotal + ":" + totalCount + ":" + list)
}

function addWhere(column, path, criteria) {
    console.log("addWhere = " + column + "/" + path + "/" + criteria)
    console.log("column.operation = " + column.getOperation())
    console.log("criteria.convertedSingleValue = " + criteria.getConvertedSingleValue())

        switch(column.getOperation()) {
            case "eq":  context.getWhere().and(path.eq(criteria.getConvertedSingleValue()))
                console.log("eq : " + criteria.getConvertedSingleValue())
                break;
            case "neq":  context.getWhere().and(path.ne(criteria.getConvertedSingleValue()))
                console.log("neq : " + criteria.getConvertedSingleValue())
                break;
            case "gt": context.getWhere().and(path.gt(criteria.getConvertedSingleValue()))
                console.log("gt : " + criteria.getConvertedSingleValue())
                break;
            case "gte": context.getWhere().and(path.goe(criteria.getConvertedSingleValue()))
                console.log("gte : " + criteria.getConvertedSingleValue())
                break;
            case "lt": context.getWhere().and(path.lt(criteria.getConvertedSingleValue()))
                console.log("lt : " + criteria.getConvertedSingleValue())
                break;
            case "lte": context.getWhere().and(path.loe(criteria.getConvertedSingleValue()))
                console.log("lte : " + criteria.getConvertedSingleValue())
                break;
            case "in": context.getWhere().and(path.in(criteria.getConvertedValues()))
                console.log("in : " + criteria.getConvertedValues())
                break;
            case "nin": context.getWhere().and(path.notIn(criteria.getConvertedValues()))
                console.log("nin : " + criteria.getConvertedValues())
                break;
            case "btn": context.getWhere().and(path.between(criteria.getMinValue(), criteria.getMaxValue()))
                console.log("btn : " + criteria.getMinValue() + ":" +  + criteria.getMaxValue())
                break;
            case "like": context.getWhere().and(path.like("%" + criteria.getConvertedSingleValue() + "%"))
                console.log("nin : " + criteria.getConvertedSingleValue())
                break;
        }
}

function addCondition(column, qClassInstance)
{
    let queryPath = qClassInstance[column.getName()]
    console.log(column.getName() + " isLeaf : " + column.isLeaf())

    if (column.isLeaf()) {
        let criteria = context.filterCriteria(column, queryPath)
        addWhere(column, queryPath, criteria)
    }else {
        let subQClassInstance = context.getQClassInstance(queryPath.getElementType())
        console.log("subQClassInstance : " + subQClassInstance.getClass())
        context.getQuery().join(queryPath, subQClassInstance)
        for (column of column.getColumns()) {
            addCondition(column, subQClassInstance)
        }
    }
}
findAll()

