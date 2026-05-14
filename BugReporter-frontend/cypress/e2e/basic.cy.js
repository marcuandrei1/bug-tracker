describe('Base Operations', () => {
  it('register', () => {
    cy.visit('http://localhost:5173/login');
    
    cy.contains('Inregistreaza-te').click();
    
    cy.url().should('include', '/register');
    cy.get('h2').should('contain', 'Inregistrare')
    
    cy.get(':nth-child(2) > input').type('UserTest')
    cy.get(':nth-child(2) > input')
      .should('have.value', 'UserTest')
    
    cy.get(':nth-child(3) > input').type('usertest@test.com')
    cy.get(':nth-child(3) > input')
      .should('have.value', 'usertest@test.com')
    
    cy.get(':nth-child(4) > input').click()
    cy.get(':nth-child(4) > input').type('test123')
    cy.get('#root button.btn').click();
    
    // cy.url()
    //   .should('eq', 'http://localhost:5173/bugs')
    
    cy.get('#root p')
      .should('contain.text', 'Username-ul exista deja')
    
    
  });

  it('login', function() {
    cy.visit('http://localhost:5173/login')
    
    cy.get('#root h2')
      .should('contain.text', 'Login')
    
    cy.get('#root button.btn')
      .should('contain.text', 'Intra in cont')
    
    cy.get('#root a')
      .should(($el) => {
        expect($el).to.have.attr('href', '/register')
        expect($el).to.contain.text('Inregistreaza-te')
      })
    
    cy.get('#root div:nth-child(2) input').type('UserTest');
    cy.get('#root div:nth-child(3) input').type('test123');
    cy.get('.btn').click();
  });


});
