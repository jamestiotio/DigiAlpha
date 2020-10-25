def RippleCarry32BC():
  print("\n") # Neat
  # Generate XB
  for i in range(32):
    print("BINV%d := NOT(Bin%d);" % (i, i)) # invert B

  print("\n")

  print("OP0INV := NOT(OP0);")

  print("\n")

  # Implement hex inversion in BC
  for i in range(1, 5):
    print("SEL%d := NOT(OP0INV);" % i)

  for i in range(1, 5):
    print("\n")

    # Implement mux selectors using AND, OR and INV
    for j in range(8):
        k = 8 * (i - 1) + j
        print("M%d := AND(Bin%d, BINV%d);" % (k, k, k))
        print("N%d := AND(SEL%d, NOT(BINV%d));" % (k, i, k))
        print("B%d := OR(M%d, N%d);" % (k, k, k))

  # Handle OP0
  print("\nC0 := (OP0);")

  # Ripple Time!
  for i in range(32):
    print("C%d := OR(AND(A%d,B%d),AND(A%d,C%d),AND(B%d,C%d));" % (i+1, i, i, i, i, i, i)) # Ci+1 = (Ai AND Bi) OR (Ai AND Ci) OR (Bi AND Ci)
  
  print("\n") # Neat
  for i in range(32):
    print("RSUM%d := ODD(A%d,B%d,C%d);" % (i, i, i, i)) # SUMi  = Ai XOR Bi XOR Ci

def KoggeStone32BC():
  print("\n") # Neat
  # Generate Propagate Cells

  for i in range(32):
    print("X%d := NOT(OR(A%d, B%d));" % (i, i, i)) # Xi = Ai NOR Bi

  print("\n")

  for i in range(32):
    print("L0P%d := NOT(OR(AND(A%d, B%d), X%d));" % (i, i, i, i)) # Pi = NOT((Ai AND Bi) OR Xi)

  print("\n") # Neat
  print("kek0 := AND(A0,B0);") # kek0 = Ai AND Bi
  print("L0G0 := OR(AND(L0P0, OP0),kek0);") # gg = (pt AND gl) OR gt
  for i in range(1,32):
    print("L0G%d := AND(A%d,B%d);" % (i, i, i)) # Gi = Ai AND Bi

  # Layers
  for l in range(5):
    print("\n") # Neat
    for i in range(2**l):
      print("L%dG%d := (L%dG%d);" % (l+1, i, l, i)) # PASSTHRU
    for i in range(2**l,32):
      print("L%dG%d := OR(AND(L%dP%d, L%dG%d),L%dG%d);" % (l+1, i, l, i, l, i-2**l, l, i)) # GG
    for i in range(2**(l+1),32):
      print("L%dP%d := AND(L%dP%d, L%dP%d);" % (l+1, i, l, i, l, i-2**l)) # PP

  # Compute Sum
  print("") # Neat
  print("KSUM0 := ODD(L0P0, OP0);") # SPECIAL CASE
  for i in range(1,32):
    print("KSUM%d := ODD(L0P%d, L5G%d);" % (i, i, i-1)) # XOR for sum

if __name__ == '__main__':
  print("BC1.1") # BC File Header
  RippleCarry32BC()
  KoggeStone32BC()

  # TEST IF MSB IS EQUAL
  print("\n\n") # Neat
  print("MUSTBEFALSE := ODD(RSUM31, KSUM31);")
  print("ASSIGN MUSTBEFALSE;")