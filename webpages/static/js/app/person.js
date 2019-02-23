(function($) {
	
	var Person = Backbone.Model.extend({
		
		urlRoot : "api/person/",
		
		  url : function(){
			    var url = this.urlRoot + this.id;
			    if(this.get("type")){
			      url = url + "/?type=" + this.get("type");
			    }
			    return url;
			  },
		
		
		
	});
	
	
	
	var PersonList = Backbone.Collection.extend({
		model: Person,
		
		url : "api/person/",	

	});
	
	
	
	var PersonListView = Backbone.View.extend({

		el: $('#person-list-container'),
		
		initialize: function() {
			this.collection.on('remove', this.render, this);
			this.collection.on('change', this.render, this);
		},
	
		render: function(){
			this.$el.html('');
			
			this.collection.each(function(person){
				console.log("person "+ person.attributes);
				var personLineView = new PersonLineView({model: person});
				
				this.$el.append(personLineView.render().el)
			}, this)

			
			return this;
		},


	});

var PersonLineView = Backbone.View.extend({
		
	tagName: 'tr',
		

		initialize: function() {
			this.template = _.template($('#PersonLine-template').html());
		},

		render: function(){
			this.$el.html(this.template(this.model.toJSON()) );
			return this;
		},

							
		remove_item: function(){
			this.collection.remove(this.model.destroy());
			
		},
});
	

	
	
	
	
	
	
	$(document).ready(function() {
		console.log('ready & able');
		
		var personList = new PersonList();
		
		personList.fetch({
			reset: true,

			success: function(){
				console.log('hello success');
				console.log(personList);
				var personListView = new PersonListView({collection: personList});
				personListView.render();
			},
			complete: function(xhr, textStatus) {
			    console.log(textStatus);
			  },
			
			error: function(model, response){
				console.log('error arguments', arguments);
			},
			
			});
		
		});
		
		

	

	
})(jQuery);