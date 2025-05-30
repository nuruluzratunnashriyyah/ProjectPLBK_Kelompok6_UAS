interface ILawyerMgt {
    -- Operations
    getLawyer() : Lawyer
    getLawyer(LawyerId) : Lawyer
    addLawyer() : boolean
    deleteLawyer() : boolean

    -- OCL Constraints
    context ILawyerMgt::getLawyer(lawyerId : String) : Lawyer
    pre: lawyerId <> null and lawyerId <> ''
    post: result <> null implies result.id_lawyer = lawyerId

    context ILawyerMgt::addLawyer() : boolean
    pre: self.isAdmin() = true
    post: result = true implies Lawyer.allInstances()->size() = Lawyer.allInstances()@pre->size() + 1

    context ILawyerMgt::deleteLawyer() : boolean
    pre: self.getLawyer() <> null and
         self.getLawyer().tersedia = true
    post: result = true implies
          Lawyer.allInstances()->size() = Lawyer.allInstances()@pre->size() - 1

    -- Invariants
    context Lawyer
    inv validLawyerData: self <> null implies self.oclIsTypeOf(Lawyer)
    
    inv completeLawyerInfo: self <> null implies
                           self.id_lawyer <> null and
                           self.nama <> null and
                           self.spesialisasi <> null and
                           self.feePerCase >= 0

    inv availabilityConsistency: self.tersedia = false implies
                                Cases.allInstances()->exists(c | 
                                    c.lawyerAssigned = self and
                                    (c.status = 'Assigned' or c.status = 'InProgress'))

    inv uniqueLawyerId: Lawyer.allInstances()->forAll(l1, l2 |
                       l1 <> l2 implies l1.id_lawyer <> l2.id_lawyer)

    inv validSpecialization: self.spesialisasi <> null and self.spesialisasi <> ''

    inv validFeePerCase: self.feePerCase > 0
}