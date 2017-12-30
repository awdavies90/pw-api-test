package pw.api.test.user

import pw.api.test.BaseApiTest
import spock.lang.*

@Unroll
class CreateUserValidation extends BaseApiTest {
	
	def "Create User Validation - Required Params Not Supplied"() {
		
		given:'A user is to be created'
			def params = [
				username:username,
			    password:password,
			    confirmPassword:confirmPassword,
			    email:email,
			    role:role
			]
					
		when:'The required fields are not all supllied'
			def response = userHelper.create(params)
		
		then:'An appropriate error response is received'
			response.errors[0] == 'The following params are required [username, password, confirmPassword, email, role]'
		
		where:
			username | password | confirmPassword | email		   | role
			null	 | 'pass12'	| 'pass12'		  | 'tstusr@x.com' | 'BAND'
			'tstusr' | null		| 'pass12'		  | 'tstusr@x.com' | 'BAND'
			'tstusr' | 'pass12' | null			  | 'tstusr@x.com' | 'BAND'
			'tstusr' | 'pass12' | 'pass12'		  | null		   | 'BAND'
			'tstusr' | 'pass12' | 'pass12'		  | 'tstusr@x.com' | null
	}
}
