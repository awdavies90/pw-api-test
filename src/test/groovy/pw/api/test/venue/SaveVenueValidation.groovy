package pw.api.test.venue

import pw.api.test.BaseApiTest
import spock.lang.*

@Unroll
class SaveVenueValidation extends BaseVenueTest {
	
	def "1 - Save Venue Validation - Required Params Not Supplied"() {
		
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
			response.errors[0] == 'The following params are required [name, address, postcode].'
		
		where:'The following parameters are supplied'
			nameSupplied | addressSupplied | postcodeSupplied | description
			false		 | true			   | true			  | 'No venue name supplied'
			true		 | false		   | true			  | 'No address supplied'
			true		 | true			   | false			  | 'No postcode supplied'
	}
	
	def "2 - Save Venue Validation - Venue With Same Name & Postcode Already Exists - System Venue"() {
		
		given:'A custom venue is to be saved'
			def existingVenue = venueHelper.getRandomVenueWithPostcode()
			
			def saveParams = [
				name:"My Unique Venue",
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'UN1 1IQ'
			]
			def invalidSaveParams = [
				name:existingVenue.name,
				address:'Save Venue Validation',
				postcode:existingVenue.postcode
			]
			println "AD PARAMS: $invalidSaveParams"
					
		when:'The venue is saved with a venue name and postcode which matches an existing system venue'
			venueHelper.saveVenue(saveParams, individualUserToken)
			def response = venueHelper.saveVenue(invalidSaveParams, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'A venue already exists with the same name and postcode.'
	}
	
	def "3 - Save Venue Validation - Venue With Same Name & Postcode Already Exists - Custom Venue"() {
		
		setup:'A custom venue which has the same name and postcode as an existing venue is to be saved'
			def setupParams = [
				name:'Duplicate Name',
				address:'Duplicate Address',
				postcode:'DU1 1PL'
			]
			def setupResponse = venueHelper.saveVenue(setupParams, individualUserToken)
				
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
