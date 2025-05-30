interface ICasesMgt {
    -- Operations
    getCase() : Cases
    getDetailCase() : Cases
    assignLawyer() : boolean

    -- OCL Constraints
    context ICasesMgt::assignLawyer() : boolean
    pre: self.getCase() <> null and
         self.getCase().lawyerAssigned = null and
         self.getCase().status = 'Open'
    post: result = true implies
          self.getCase().lawyerAssigned <> null and
          self.getCase().status = 'Assigned'

    -- Invariants
    context Cases
    inv validCaseData: self <> null implies self.oclIsTypeOf(Cases)
    
    inv completeCaseDetails: self <> null implies 
                            self.id_case <> null and
                            self.caseTitle <> null and
                            self.caseDescription <> null and
                            self.status <> null and
                            self.fee >= 0 and
                            self.client <> null
    
    inv validCaseState: self.status = 'Open' or 
                       self.status = 'Assigned' or 
                       self.status = 'InProgress' or 
                       self.status = 'Closed'

    inv validFee: self.fee >= 0
    
    inv assignedLawyerConsistency: self.status = 'Assigned' implies
                                  self.lawyerAssigned <> null
    
    inv openCaseConsistency: self.status = 'Open' implies
                            self.lawyerAssigned = null