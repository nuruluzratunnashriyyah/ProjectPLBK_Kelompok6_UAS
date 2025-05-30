interface IClientMgt {
    -- Operations
    getClient() : Client
    getClientInfo() : Client  
    deleteClient() : boolean
    getClientByEmail(String email) : Client

    -- OCL Constraints
    context IClientMgt::deleteClient() : boolean
    pre: self.getClient() <> null
    post: result = true implies self.getClient() = null

    context IClientMgt::getClientByEmail(email : String) : Client
    pre: email <> null and email <> ''
    post: result <> null implies result.email = email

    -- Invariants
    context Client
    inv validClientData: self <> null implies self.oclIsTypeOf(Client)
    
    inv completeClientInfo: self <> null implies
                           self.id_client <> null and
                           self.nama <> null and
                           self.kontak <> null and
                           self.email <> null and
                           self.password <> null and
                           self.alamat <> null

    inv validEmail: self.email.matches('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')

    inv uniqueEmail: Client.allInstances()->forAll(c1, c2 | 
                    c1 <> c2 implies c1.email <> c2.email)

    inv validContact: self.kontak <> null and self.kontak <> ''

    inv clientCaseConsistency: self.case->notEmpty() implies
                              self.case->forAll(c | c.client = self)
}