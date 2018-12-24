package lambda.filter.impl;

import lambda.domain.Employer;
import lambda.filter.FilterEmployer;

/**
 * 按照年龄过滤
 */
public class FilterEmployerByAgeImpl implements FilterEmployer<Employer> {

    //返回薪水大于24的实体
    public boolean filter(Employer employer) {
        return employer.getAge() >= 24;
    }
}
