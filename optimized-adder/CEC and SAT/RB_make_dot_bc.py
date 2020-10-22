# RAGUL BALAJI 2020
# SUTD 50.002 Computation Structures 2D WEEK!
# Print BC file stuffs to STDOUT

def RippleCarry32BC():
  print("\n") # Neat
  # Generate XB
  for i in range(32):
    print("B%d := ODD(Bin%d,OP0);" % (i, i)) # invert B if op0

  # Handle OP0
  print("\nC0 := (OP0);")

  # Ripple Time!
  for i in range(32):
    print("C%d := OR(AND(A%d,B%d),AND(A%d,C%d),AND(B%d,C%d));" % (i+1, i, i, i, i, i, i)) # Ci+1 = (Ai AND Bi) OR (Ai AND Ci) OR (Bi AND Ci)
  
  print("") # Neat
  for i in range(32):
    print("RSUM%d := ODD(A%d,B%d,C%d);" % (i, i, i, i)) # SUMi  = Ai XOR Bi XOR Ci

def KoggeStone32BC():
  print("\n") # Neat
  # Generate Propgate Cells

  for i in range(32):
    print("L0P%d := ODD(A%d,B%d);" % (i, i, i)) # Pi = Ai XOR Bi

  print("") # Neat
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
  