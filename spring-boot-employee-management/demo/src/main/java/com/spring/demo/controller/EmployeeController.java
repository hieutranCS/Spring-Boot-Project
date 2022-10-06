package com.spring.demo.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.demo.dao.SearchEmployeeDAO;
import com.spring.demo.entity.Address;
import com.spring.demo.entity.Department;
import com.spring.demo.entity.Employee;
import com.spring.demo.model.SearchEmployee;
import com.spring.demo.repository.EmployeeRepository;
import com.spring.demo.service.AddressService;
import com.spring.demo.service.DepartmentService;
import com.spring.demo.service.EmployeeService;

@Controller
@Transactional
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private SearchEmployeeDAO searchEmployeeDAO;

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/")
	public String employeeHome(Model model) {
		return employeePage(1, "firstName", "asc", model);
	}

	@GetMapping("/search")
	public String searchEmployee(Model model, String keyword) {
		return searchPage(1, "firstName", "asc", model, keyword);
	}

	@GetMapping("/newEmployeeForm")
	public String newEmployeeForm(Model model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "add_employee";
	}

	@GetMapping("/employeeDetail/{id}")
	public String showEmployeeDetail(@PathVariable(value = "id") long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);

		Address address = addressService.getAddressByEmployeeId(employee.getEmployeeId());
		Department department = departmentService.getDepartmentByEmployeeId(employee.getEmployeeId());

		model.addAttribute("employee", employee);
		model.addAttribute("address", address);
		model.addAttribute("department", department);

		return "view";
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee,
			@ModelAttribute("department") Department department,
			@ModelAttribute("address") Address address, Model model) {
		Employee employeeExists = employeeRepository.findByEmail(employee.getEmail());
		if (employeeExists != null) {
			model.addAttribute("errorEmail", "An employee with this email already exists.");
			return "add_employee";
		} else {
			String validEmployee = employeeService.validateEmployee(employee);
			if (validEmployee == "errorPhone") {
				model.addAttribute("errorPhone", "This phone number is invalid.");
				return "add_employee";
			} else if (validEmployee == "errorEmail") {
				model.addAttribute("errorEmail", "This email is invalid.");
				return "add_employee";
			} else {
				employeeService.saveEmployee(employee);
				return "redirect:/";
			}
		}
	}

	@PostMapping("/updateEmployee")
	public String updateEmployee(@ModelAttribute("employee") Employee employee,
			@ModelAttribute("department") Department department,
			@ModelAttribute("address") Address address) {
		employeeService.saveEmployee(employee);
		departmentService.saveDepartment(department, employee);
		addressService.saveAddress(address, employee);
		return "redirect:/";
	}

	@GetMapping("/employeeUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		Address address = addressService.getAddressByEmployeeId(employee.getEmployeeId());
		Department department = departmentService.getDepartmentByEmployeeId(employee.getEmployeeId());
		model.addAttribute("employee", employee);
		model.addAttribute("address", address);
		model.addAttribute("department", department);

		return "update";
	}

	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id) {
		this.addressService.deleteAddressByEmployeeId(id);
		this.departmentService.deleteDepartmentByEmployeeId(id);
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/";
	}

	@GetMapping("/page/{pageNo}")
	public String employeePage(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 5;

		Page<Employee> page = this.employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("listEmployees", listEmployees);

		return "index";
	}

	@GetMapping("/search/page/{pageNo}")
	public String searchPage(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model,
			@RequestParam(name = "key", defaultValue = "") String keyword) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Page<Object[]> items = searchEmployeeDAO.getAllSearchEmployees(entityManager, keyword,PageRequest.of(pageNo- 1, 5, sort));
		List<SearchEmployee> employeeList = new ArrayList<>();

        for (Object[] item: items) {
            SearchEmployee c = new SearchEmployee();
            c.setEmployeeId(String.valueOf(item[0]));
            c.setFirstName(String.valueOf(item[1]));
            c.setLastName(String.valueOf(item[2]));
			c.setTitle(String.valueOf(item[3]));
			c.setEmail(String.valueOf(item[4]));
			c.setDepartmentName(String.valueOf(item[5]));
			c.setPosition(String.valueOf(item[6]));
            employeeList.add(c);
        }

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", items.getTotalPages());
		model.addAttribute("totalItems", items.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("key", keyword);
		model.addAttribute("listEmployees", employeeList);



		return "index";
	}

}
