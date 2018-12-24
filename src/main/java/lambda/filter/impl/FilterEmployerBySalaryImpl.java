package lambda.filter.impl;

import lambda.domain.Employer;
import lambda.filter.FilterEmployer;

/**
 * 按照薪水过滤
 */
public class FilterEmployerBySalaryImpl implements FilterEmployer<Employer> {

    //返回薪水大于4000的实体
    public boolean filter(Employer employer) {
        return employer.getSalary() >= 4000;
    }
}
