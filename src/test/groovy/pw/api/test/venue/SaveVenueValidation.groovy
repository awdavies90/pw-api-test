package pw.api.test.venue

import pw.api.test.BaseApiTest
import spock.lang.*

@Unroll
class SaveVenueValidation extends BaseVenueTest {
	
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
		
		where:'The following parameters are supplied'
			nameSupplied | addressSupplied | postcodeSupplied | description
			false		 | true			   | true			  | 'No venue name supplied'
			true		 | false		   | true			  | 'No address supplied'
			true		 | true			   | false			  | 'No postcode supplied'
	}
	
	@IgnoreRest
	def "Save Venue Validation - Venue With Same Name & Postcode Already Exists"() {
		
		setup:'A custom venue which has the same name and postcode as an existing venue is to be saved'
			def setupParams = [
				name:'Duplicate Name',
				address:'Duplicate Address',
				postcode:'DU1 1PL'
			]
			def setupResponse = venueHelper.saveVenue(setupParams, individualUserToken)
		
		//given:'A custom venue which has the same name and postcode as an existing venue is to be saved'
					
		when:'The venue is saved'
			def params = [
				name:name,
				address:address,
				postcode:postcode
			]
			def response = venueHelper.saveVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == valid ? 200 : 400
			response.errors?.getAt(0) == valid ? null : 'A venue already exists with the same name and postcode.'
		
		cleanup:
			if (setupResponse.id)
				venueHelper.deleteVenue(setupResponse.id)
			if (response.id)
				venueHelper.deleteVenue(response.id)
			
		where:'The following parameters are supplied'
			name 			 | address 			   | postcode  | valid
			'Duplicate Name' | 'Duplicate Address' | 'DU1 1PL' | false
			'Duplicate Name' | 'Diff Address'	   | 'DU1 1PL' | false
			'Diff Name' 	 | 'Duplicate Address' | 'DU1 1PL' | true
			'Duplicate Name' | 'Duplicate Address' | 'AB1 1CD' | true
	}
}
