# Copyright 2016 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from google.appengine.ext import ndb
import webapp2
import json

class Boardgame(ndb.Model):
    id = ndb.StringProperty()
    title = ndb.StringProperty(required=True)
    description = ndb.StringProperty(required=True)
    stock = ndb.IntegerProperty(required=True)
    price = ndb.IntegerProperty(required=True)

class Customer(ndb.Model):
    id = ndb.StringProperty()
    firstName = ndb.StringProperty(required=True)
    lastName = ndb.StringProperty(required=True)
    money = ndb.IntegerProperty(required=True)
    capacity = ndb.IntegerProperty(required=True)
    inventory = ndb.StringProperty(repeated=True)

class BoardgameHandler(webapp2.RequestHandler):
    def post(self):
        bg_data = json.loads(self.request.body)
        if len(bg_data) == 4 and "title" in bg_data and "description" in bg_data and "stock" in bg_data and "price" in bg_data:
            if type(bg_data['title']) == unicode and type(bg_data['description']) == unicode and type(bg_data['stock']) == int and type(bg_data['price']) == int:
                new_bg = Boardgame(title=bg_data['title'], description=bg_data['description'], stock=bg_data['stock'], price=bg_data['price'])
                new_bg.put()
                new_bg.id = '' + new_bg.key.urlsafe()
                new_bg.put()
                bg_dict = new_bg.to_dict()
                bg_dict['self'] = '/boardgame/' + new_bg.key.urlsafe()
                self.response.write(json.dumps(bg_dict))
                self.response.set_status(201)
            else:
                self.response.write("Invalid data types")
                self.response.set_status(400)
        else:
            self.response.write("Invalid body")
            self.response.set_status(400)

    def get(self, id=None):
        if id:
            query = Boardgame.query(Boardgame.id == id)
            bg = query.get()
            if bg != None:
                bg_dict = bg.to_dict()
                bg_dict['self'] = "/boardgame/" + id
                self.response.write(json.dumps(bg_dict))
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)
        else:
            boardgames = Boardgame.query().fetch()
            boardgame_list = []
            for b in boardgames:
                test_dict = b.to_dict()
                test_dict['self'] = '/boardgame/' + b.id
                boardgame_list.append(test_dict)
            self.response.write(json.dumps(boardgame_list))
      
    def patch(self, id=None):
        if id:
            query = Boardgame.query(Boardgame.id == id)
            bg = query.get()
            if bg != None:
                bg_data = json.loads(self.request.body)
                if len(bg_data) == 4 and "title" in bg_data and "description" in bg_data and "stock" in bg_data and "price" in bg_data:
                    if type(bg_data['title']) == unicode and type(bg_data['description']) == unicode and type(bg_data['stock']) == int and type(bg_data['price']) == int:
                        bg.title = bg_data['title']
                        bg.description = bg_data['description']
                        bg.stock = bg_data['stock']
                        bg.price = bg_data['price']
                        bg.put()
                        self.response.set_status(204)
                    else:
                        self.response.write("Invalid data types")
                        self.response.set_status(400)
                else:
                    self.response.write("Invalid request")
                    self.response.set_status(400)
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

    def delete(self, id=None):
        if id:
            bg_query = Boardgame.query(Boardgame.id == id)
            bg = bg_query.get()
            if bg != None:
                self.response.set_status(204)
                bg.key.delete()
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

    def put(self, id=None):
        if id:
            bg_query = Boardgame.query(Boardgame.id == id)
            bg = bg_query.get()
            if bg != None:
                customer_data = json.loads(self.request.body)
                if len(customer_data) == 1 and 'id' in customer_data:
                    if type(customer_data['id']) == unicode:
                        query = Customer.query(Customer.id == customer_data['id'])
                        customer = query.get()
                        if customer != None:
                            if customer.money >= bg.price and bg.stock > 0 and customer.capacity > len(customer.inventory):
                                customer.money = customer.money - bg.price
                                customer.inventory.append('silicon-perigee-191721.appspot.com/boardgame/' + bg.id)
                                customer.put()
                                bg.stock = bg.stock - 1
                                bg.put()
                                self.response.set_status(204)
                            else:
                                self.response.set_status(403)
                        else:
                            self.response.write("Invalid Customer ID")
                            self.response.set_status(400)
                    else:
                        self.response.write("Invalid data types")
                        self.response.set_status(400)
                else:
                    self.response.write("Invalid Request")
                    self.response.set_status(400)
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

class CustomerHandler(webapp2.RequestHandler):
    def post(self):
        customer_data = json.loads(self.request.body)
        if len(customer_data) == 4 and "firstName" in customer_data and "lastName" in customer_data and "money" in customer_data and "capacity" in customer_data:
            if type(customer_data['firstName']) == unicode and type(customer_data['lastName']) == unicode and type(customer_data['money']) == int and type(customer_data['capacity']) == int:
                new_customer = Customer(firstName=customer_data['firstName'], lastName=customer_data['lastName'], money=customer_data['money'], capacity=customer_data['capacity'])
                new_customer.put()
                new_customer.id = '' + new_customer.key.urlsafe()
                new_customer.put()
                customer_dict = new_customer.to_dict()
                customer_dict['self'] = '/customer/' + new_customer.key.urlsafe()
                self.response.set_status(201)
                self.response.write(json.dumps(customer_dict))
            else:
                self.response.write("Invalid data types")
                self.response.set_status(400)
        else:
            self.response.write("Invalid request")
            self.response.set_status(400)

    def get(self, id=None):
        if id:
            query = Customer.query(Customer.id == id)
            customer = query.get()
            if customer != None: 
                customer_dict = customer.to_dict()
                customer_dict['self'] = '/customer/' + id
                self.response.write(json.dumps(customer_dict))
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)
        else:
            customers = Customer.query().fetch()
            customer_list = []
            for c in customers:
                temp_dict = c.to_dict()
                temp_dict['self'] = '/customer/' + c.id
                customer_list.append(temp_dict)
            self.response.write(json.dumps(customer_list))

    def patch(self, id=None):
        if id:
            customer_query = Customer.query(Customer.id == id)
            customer = customer_query.get()
            if customer != None:
                customer_data = json.loads(self.request.body)
                if len(customer_data) == 4 and 'firstName' in customer_data and 'lastName' in customer_data and 'money' in customer_data and 'capacity' in customer_data:
                    if type(customer_data['firstName']) == unicode and type(customer_data['lastName']) == unicode and type(customer_data['money']) == int and type(customer_data['capacity']) == int:
                        customer.firstName = customer_data['firstName']
                        customer.lastName = customer_data['lastName']
                        customer.money = customer_data['money']
                        customer.capacity = customer_data['capacity']
                        customer.put()
                        self.response.set_status(204)
                    else:
                        self.response.write("Invalid data types")
                        self.response.set_status(400)
                else:
                    self.response.write("Invalid Request")
                    self.response.set_status(400)
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

    def delete(self, id=None):
        if id:
            customer_query = Customer.query(Customer.id == id)
            customer = customer_query.get()
            if customer != None:
                self.response.set_status(204)
                customer.key.delete()
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

    def put(self, id=None):
        if id:
            customer_query = Customer.query(Customer.id == id)
            customer = customer_query.get()
            if customer != None:
                bg_data = json.loads(self.request.body)
                if len(bg_data) == 1 and 'id' in bg_data:
                    if type(bg_data['id']) == unicode:
                        bg_query = Boardgame.query(Boardgame.id == bg_data['id'])
                        bg = bg_query.get()
                        if bg != None:
                            customer.money = customer.money + bg.price
                            bg.stock = bg.stock + 1
                            customer.inventory.remove('silicon-perigee-191721.appspot.com/boardgame/' + bg.id)
                            bg.put()
                            customer.put()
                            self.response.set_status(204)
                        else:
                            self.response.write("Invalid Boardgame ID")
                            self.response.set_status(400)
                    else:
                        self.response.write("Invalid data")
                        self.response.set_status(400)
                else:
                    self.response.write("Invalid body")
                    self.response.set_status(400)
            else:
                self.response.write("Invalid ID")
                self.response.set_status(400)

class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write("CS 496 Final Project Main Page")

allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods
app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/boardgame', BoardgameHandler),
    ('/boardgame/(.*)', BoardgameHandler),
    ('/customer', CustomerHandler),
    ('/customer/(.*)', CustomerHandler)
], debug=True)
