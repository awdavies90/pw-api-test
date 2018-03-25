package pw.api.test.venue

import pw.api.test.BaseApiTest
import spock.lang.*

class SaveVenueValidation extends BaseVenueTest {
	
	@Unroll
	def "Save Venue Validation - Required Params Not Supplied"() {
		
		given:"A custom venue which doesn't have all the required fields is to be saved"
			def params = [
				name: nameSupplied ? 'New Custom Venue' : null,
				address: addressSupplied ? 'Nowhere Street, Nowherarea, Nowherecity' : null,
				postcode: postcodeSupplied ? 'VE1 2NU' : null
			]
					
		when:'The venue is saved'
			def response = venueHelper.saveVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'The following params are required [name, address, postcode]'
		
		where:
			nameSupplied | addressSupplied | postcodeSupplied | description
			false		 | true			   | true			  | 'No venue name supplied'
			true		 | false		   | true			  | 'No address supplied'
			true		 | true			   | false			  | 'No postcode supplied'
	}
	
	def "Save Venue Validation - Venue With Same Name & Postcode Already Exists"() {
		
		given:'A custom venue which has the same name and postcode as an existing venue is to be saved'
			def params = [
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is saved'
			venueHelper.saveVenue(params, individualUserToken)
			def response = venueHelper.saveVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'A venue already exists with the same name and postcode.'
	}
}
