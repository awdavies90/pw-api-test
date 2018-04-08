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
	
	@Ignore //TODO: Finish this test
	def "Update Venue Validation - Venue With Same Name & Postcode Already Exists"() {
		
		given:'A custom venue is to be updated'
			def params = [
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is updated to have the same address as an existing venue'
			venueHelper.saveVenue(params, individualUserToken)
			def response = venueHelper.saveVenue(params, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'A venue already exists with the same name and postcode.'
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
