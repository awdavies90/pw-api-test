package pw.api.test.venue

import java.util.concurrent.atomic.AtomicInteger

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import spock.lang.*

class UpdateVenueValidation extends BaseVenueTest {
	
	def "Update Venue Validation - No ID Supplied"() {
		
		given:"A custom venue is to be updated"
			def params = [
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is updated but no id is supplied'
			def response = venueHelper.updateVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'id must be of type INT.'
	}
	
	def "Update Venue Validation - ID Does Not Exist"() {
		
		given:"A custom venue is to be updated"
			def params = [
				id:-1,
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is updated but the supplied id does not exist'
			def response = venueHelper.updateVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'No venue was found with this ID.'
	}
	
	def "Update Venue Validation - Required Params Not Supplied"() {
		
		given:"A custom venue is to be updated"
			def params = [
				id:venueHelper.getRandomCustomVenueId(individualUserToken),
				name:null,
				address:null,
				postcode:null
			]
					
		when:'The venue is updated without all the required values'
			def response = venueHelper.updateVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'One of name, address or postcode must be supplied.'
	}
	
	def "Update Venue Validation - Update Non Custom Venue"() {
		
		given:"A custom venue is to be updated"
			def params = [
				id:venueHelper.getRandomVenueId(individualUserToken),
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is a non-custom venue'
			def response = venueHelper.updateVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'You cannot update a non-custom venue.'
	}
	
	@IgnoreRest
	def "Update Venue Validation - Venue With Same Name & Postcode Already Exists - System Venue"() {
		
		given:'A custom venue is to be updated'
			def existingVenue = venueHelper.getRandomVenueWithPostcode()
			
			def saveParams = [
				name:"My Unique Venue",
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'UN1 1QE'
			]
			def updateParams = [
				name:existingVenue.name,
				postcode:existingVenue.postcode
			]
					
		when:'The venue is updated with a venue name and postcode which matches an existing system venue'
			def saveResponse = venueHelper.saveVenue(saveParams, individualUserToken)
			updateParams.id = saveResponse.id
			def response = venueHelper.updateVenue(updateParams, individualUserToken) 
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'A venue already exists with the same name and postcode.'
	}
	
	@Unroll
	def "Update Venue Validation - Venue With Same Name & Postcode Already Exists - Custom Venue"() {
		
		setup:'A custom venue is to be updated'
			def setupParams = [
				name:'Duplicate Name',
				address:'Duplicate Address',
				postcode:'DU1 1PL'
			]
			def setupResponse = venueHelper.saveVenue(setupParams, individualUserToken)
			
		when:'The venue is updated with a venue name and postcode which matches an existing custom venue'
			def params = [
				id:setupResponse?.id,
				name:name,
				address:address,
				postcode:postcode
			]
			def response = venueHelper.updateVenue(params, individualUserToken)
		
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
	
	//----------Permissions Tests----------//
	def "Update Venue Permissions - Update Another User's Custom Address"() {
		
		given:"A custom venue is to be updated"
			def saveParams = [
				name:"New Custom Venue ${new Date()}",
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
			def saveResponse = venueHelper.saveVenue(saveParams, individualUserToken)
					
		when:'The venue is a is a custom'
			def updateParams = [
				id:saveResponse.id,
				name:"Updated New Custom Venue ${new Date()}",
				address:'Updated Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
			def response = venueHelper.updateVenue(updateParams, individualUserToken2)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'You can not update a venue which another user has created.'
	}
}
