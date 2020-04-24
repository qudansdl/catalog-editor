if (!Object.prototype.getClassName) {
    Object.prototype.getClassName = function () {
        return Object.prototype.toString.call(this).match(/^\[object\s(.*)\]$/)[1];
    }
}

function findAll()
{
    let qClassInstance = context.getQClassInstance()
    let recordsTotal = context.getQuery().select(context.getQClassInstance().id.countDistinct()).fetchOne()

    for (column of context.getInput().getColumns()) {
        addCondition(column, qClassInstance)
    }

    context.getQuery().where(context.getWhere())

    let totalCount = context.getQuery().select(context.getQClassInstance().id.countDistinct()).fetchOne()
    let list = context.getQuery().select(context.getQClassInstance()).fetch()


    context.getOutput().setRecordsTotal(recordsTotal)
    context.getOutput().setRecordsFiltered(totalCount)
    context.getOutput().setData(list)
    console.log(recordsTotal + ":" + totalCount + ":" + list)
}

function addWhere(column, path, criteria) {
        switch(column.operation) {
            case "eq": path.eq(criteria.convertedSingleValue)
                break;
            case "neq":  path.ne(criteria.convertedSingleValue)
                break;
            case "gt": path.gt(criteria.convertedSingleValue)
                break;
            case "gte": path.goe(criteria.convertedSingleValue)
                break;
            case "lt": path.lt(criteria.convertedSingleValue)
                break;
            case "lte": path.loe(criteria.convertedSingleValue)
                break;
            case "in": path.in(criteria.convertedValues)
                break;
            case "nin": path.notIn(criteria.convertedValues)
                break;
            case "btn": path.between(criteria.minValue, criteria.maxValue)
                break;
            case "like": path.like("%" + criteria.convertedSingleValue + "%")
                break;
        }
}

function addCondition(column, qClassInstance)
{
    let queryPath = qClassInstance[column.getName()]
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

