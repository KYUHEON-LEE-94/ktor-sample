package sample.study.domain.people

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @Description : Employee.java
 * @author      : heon
 * @since       : 24. 10. 7.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 7.       heon         최초 생성
 *
 * <pre>
 */

data class Employee(val name:String, val age:Int, val deptName:String) {
    //동반 객체는 특정 클래스와 연결된 싱글톤 객체로, 클래스의 인스턴스 없이도 해당 클래스와 관련된 함수나 속성에 접근할 수 있도록 해줍니다.
    companion object{

        private val employees = mutableMapOf<String, Employee>()

        fun save(e: Employee): Employee {
            employees[e.name]?.let {
                employees.replace(e.name, e)
            } ?: run {
                val previousValue = employees.put(e.name, e)
                if (previousValue == null) {
                    println("New employee added.")
                }
            }
            return e
        }



        fun delete(name:String) = employees.remove(name)
        fun find(name:String): Employee = employees[name] ?: throw NotFoundException("Employee $name not found")
        fun updateDeptNameByName(name:String, deptName:String): Employee =
            find(name).run {
                val newEm = Employee(name = this.name, age= this.age, deptName = deptName)
                save(newEm)
            }
    }
}

fun Route.employeeRoutes(){
    post("/employees") {
        val employee = call.receive<Employee>()
        println("employee: ${employee.name}, ${employee.age}")
        println("employee: $employee")
        val saved = Employee.save(employee)
        call.respond(HttpStatusCode.Created, saved)
    }

    get("/employees/{name}") {
        val name = call.parameters["name"]?: throw BadRequestException("name is missing")

        call.respond(Employee.find(name))
    }

    put("/employees/{name}") {
        val name = call.parameters["name"]?: throw BadRequestException("name is missing")
        val deptName = call.parameters["deptName"] ?: throw BadRequestException("deptName is missing")

        call.respond(Employee.updateDeptNameByName(name, deptName))
    }

    delete("/employees"){
        val name = call.parameters["name"]?:throw BadRequestException("name is missing")
        Employee.delete(name)
        call.respond(HttpStatusCode.OK)
    }
}
/**
 * curl -v -X POST http://localhost:8080/employees -H 'Content-Type: application/json' -d '{ "name": "Kim", "age": 30, "deptName": "IT" }'
 * curl -v -X GET http://localhost:8080/employees/Kim
 * curl -v -X PUT 'http://localhost:8080/employees/Kim?deptName=HR' -H 'Content-Type: application/json'
 * curl -v -X GET http://localhost:8080/employees/Kim
 * curl -v -X DELETE 'http://localhost:8080/employees/Kim'
 *
 * **/