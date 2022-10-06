package com.spring.demo.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Transactional
@Repository
public class SearchEmployeeDAO {
        public Page<Object[]> getAllSearchEmployees(EntityManager em, String keyword,
                        Pageable pageable) {
                String order = StringUtils.collectionToCommaDelimitedString(
                                StreamSupport.stream(pageable.getSort().spliterator(), false)
                                                .map(o -> o.getProperty() + " " + o.getDirection())
                                                .collect(Collectors.toList()));

                String sql = String.format(
                                " SELECT e.employee_id employeeId ,e.first_name firstName, e.last_name lastName, e.title title, e.email email , d.name departmentName, d.position position FROM "
                                                +
                                                " Employee e left join "
                                                + " Department d on e.employee_id = d.employee_id left join " +
                                                " Address a on e.employee_id = a.employee_id where e.birthday like :keyword or "
                                                +
                                                " e.date_hire like :keyword or e.email like :keyword or e.first_name like :keyword or "
                                                +
                                                " e.gender like :keyword or e.last_name like :keyword or e.phone like :keyword or "
                                                +
                                                " e.title like :keyword or e.type like :keyword or d.name like :keyword or d.position like :keyword or "
                                                +
                                                " d.supervisor like :keyword or a.city like :keyword or a.state like :keyword or a.street like :keyword or "
                                                +
                                                " a.zipcode like :keyword union " +
                                                " SELECT  e.employee_id employeeId ,e.first_name firstName, e.last_name lastName, e.title title, e.email email , d.name departmentName, d.position position FROM "
                                                +
                                                " Employee e right join "
                                                + " Department d on  e.employee_id = d.employee_id right join " +
                                                " Address a on e.employee_id = a.employee_id where e.birthday like :keyword or e.date_hire like :keyword or "
                                                +
                                                " e.email like :keyword or e.first_name like :keyword or e.gender like :keyword or e.last_name like :keyword or "
                                                +
                                                " e.phone like :keyword or e.title like :keyword or e.type like :keyword or d.name like :keyword or d.position like :keyword or "
                                                +
                                                " d.supervisor like :keyword or a.city like :keyword or a.state like :keyword or a.street like :keyword or a.zipcode like :keyword  Order By %s", order);

                Query query = em.createNativeQuery(sql);
                query.setParameter("keyword", "%" + keyword + "%");
                query.setMaxResults(pageable.getPageSize());
                query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

                String sql2 = " SELECT count(e.employee_id) FROM "
                                +
                                " Employee e left join "
                                + " Department d on e.employee_id = d.employee_id left join " +
                                " Address a on e.employee_id = a.employee_id where e.birthday like :keyword or "
                                +
                                " e.date_hire like :keyword or e.email like :keyword or e.first_name like :keyword or "
                                +
                                " e.gender like :keyword or e.last_name like :keyword or e.phone like :keyword or "
                                +
                                " e.title like :keyword or e.type like :keyword or d.name like :keyword or d.position like :keyword or "
                                +
                                " d.supervisor like :keyword or a.city like :keyword or a.state like :keyword or a.street like :keyword or "
                                +
                                " a.zipcode like :keyword union " +
                                " SELECT  e.employee_id,e.first_name, e.last_name, e.title, e.email, d.name, d.position FROM "
                                +
                                " Employee e right join "
                                + " Department d on  e.employee_id = d.employee_id right join " +
                                " Address a on e.employee_id = a.employee_id where e.birthday like :keyword or e.date_hire like :keyword or "
                                +
                                " e.email like :keyword or e.first_name like :keyword or e.gender like :keyword or e.last_name like :keyword or "
                                +
                                " e.phone like :keyword or e.title like :keyword or e.type like :keyword or d.name like :keyword or d.position like :keyword or "
                                +
                                " d.supervisor like :keyword or a.city like :keyword or a.state like :keyword or a.street like :keyword or a.zipcode like :keyword  ";

                Query count = em.createNativeQuery(sql2);
                count.setParameter("keyword", "%" + keyword + "%");

                long countId = (long) count.getFirstResult();

                List<Object[]> results = query.getResultList();

                PageImpl<Object[]> pages = new PageImpl<Object[]>(results, pageable,countId);

                return pages;

        }

}
