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
		
		where:'The following inputs are used'
			username | password | confirmPassword | email		   | role
			null	 | 'pass12'	| 'pass12'		  | 'tstusr@x.com' | 'BAND'
			'tstusr' | null		| 'pass12'		  | 'tstusr@x.com' | 'BAND'
			'tstusr' | 'pass12' | null			  | 'tstusr@x.com' | 'BAND'
			'tstusr' | 'pass12' | 'pass12'		  | null		   | 'BAND'
			'tstusr' | 'pass12' | 'pass12'		  | 'tstusr@x.com' | null
	}
	
	def "Create User Validation - User With Same Username Already Exists"() {
		
		given:'A user is to be created'
			def params = userHelper.getRandomUser('BAND')
			def params2 = userHelper.getRandomUser('BAND')
			params2.username = params.username
					
		when:'The suplied username already exists for another user'
			userHelper.create(params)
			def response = userHelper.create(params2)
		
		then:'An appropriate error response is received'
			response.errors[0] == 'An account already exists with this username.'
	}
	
	def "Create User Validation - User With Same Email Already Exists"() {
		
		given:'A user is to be created'
			def params = userHelper.getRandomUser('BAND')
			def params2 = userHelper.getRandomUser('BAND')
			params2.email = params.email
					
		when:'The suplied email already exists for another user'
			userHelper.create(params)
			def response = userHelper.create(params2)
		
		then:'An appropriate error response is received'
			response.errors[0] == 'An account already exists with this email address.'
	}
	
	def "Create User Validation - Password & Confirm Password Do Not Match"() {
		
		given:'A user is to be created'
			def params = userHelper.getRandomUser('BAND')
			params.confirmPassword = params.password + '1'
					
		when:'The two supplied passwords do not match'
			def response = userHelper.create(params)
		
		then:'An appropriate error response is received'
			response.errors[0] == 'The two passwords do not match.'
	}
	
	def "Create User Validation - Supplied Role Does Not Exist"() {
		
		given:'A user is to be created'
			def params = userHelper.getRandomUser('Test')
					
		when:'The supplied role does not exist'
			def response = userHelper.create(params)
		
		then:'An appropriate error response is received'
			response.errors[0] == 'The role supplied does not exist.'
	}
}
